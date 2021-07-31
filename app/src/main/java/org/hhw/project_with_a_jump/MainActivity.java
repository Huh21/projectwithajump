package org.hhw.project_with_a_jump;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest; // 앱이 시스템 또는 다른 앱의 보호된 부분에 액세스하기 위해 필요한 권한 설정 파일
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
{
    private GpsTracker gpsTracker;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    // fine_location : coarse보다 더 정확한 위치 제공, coarse_location : 도시 블록 내에 위치 정확성 제공


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textview_address = (TextView) findViewById(R.id.textview_address); // 사용자의 현재 위치 도로명주소
        final TextView user_name = (TextView) findViewById(R.id.user_name); // 사용자명
        final TextView welcome = (TextView) findViewById(R.id.welcome);

        EditText editText = (EditText) findViewById(R.id.editText);

        if (!checkLocationServicesStatus()) { // GPS 활성화가 되어 있지 않다면
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
            textview_address.setText(getGpsTracker());
        }

        Intent intentFromCertification = getIntent(); // 본인인증 화면으로부터 사용자명 받아오기
        user_name.setText(intentFromCertification.getStringExtra("사용자명"));

        //Intent intentFromOwner = getIntent(); // 등록된 사업자 리스트 받아오기
        // Facility facility = (Facility)intentFromOwner.getSerializableExtra("facility");

        Button ShowLocationButton = (Button) findViewById(R.id.show); // 시설 찾기
        Button FindLocationButton = (Button) findViewById(R.id.find); // 직접 검색

        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                textview_address.setText(getGpsTracker()); // 사용자의 현재 위치 보여줌

                String facilityName = findFacility(getGpsTracker(), "show");

                if ((facilityName).equals("not found")) {
                    Toast.makeText(MainActivity.this, "현재 위치에 해당하는 시설을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                }
                else {
                    // 현재 위치를 기반으로 명부를 가진 시설을 찾아서 시설명을 사용자에게 보여줌
                    Toast.makeText(MainActivity.this, facilityName+" 입장", Toast.LENGTH_LONG).show();

                    // 해당 시설의 명부 화면으로 이동
                    Intent secIntent = new Intent(getApplicationContext(), EntryActivity.class);
                    secIntent.putExtra("facilityName", facilityName);
                    startActivity(secIntent);
                }
            }
        });

        FindLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                String input = editText.getText().toString();

                if(input.equals("")) { // 입력칸이 빈칸이라면
                    Toast.makeText(MainActivity.this, "시설명을 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    String facilityName = findFacility(input, "find");

                    if ((facilityName).toString().equals("not found")) {
                        Toast.makeText(MainActivity.this, "해당 시설의 명부가 존재하지 않습니다.\n"
                                + "담당자에게 문의하세요.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, facilityName+" 입장", Toast.LENGTH_LONG).show();

                        // 해당 시설의 명부 화면으로 이동
                        Intent secIntent = new Intent(getApplicationContext(), EntryActivity.class);
                         secIntent.putExtra("facilityName", facilityName);
                         startActivity(secIntent);
                    }
                }
            }
        });
    }

    // 주소나 사용자의 입력값을 통해 시설명을 찾아주는 함수
    public String findFacility (String data, String who) {
        String facilityName = "";

        switch (who) {
            case "show":
                facilityName = "show";
                break;

            case "find":
                facilityName = "find";
                break;
        }

        return facilityName;
    }

    // 현재 위치의 주소 받아오는 메소드
    public String getGpsTracker() {
        gpsTracker = new GpsTracker(MainActivity.this);

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        String address = getCurrentAddress(latitude, longitude);
        return address;
    }

    // onRequestPermissionsResult의 결과를 리턴받는 메소드

    public void onRequestPermssionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            // 권한이 모두 허용됐는지 확인
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                // 권한이 모두 설정됨 -> 위치 가져오기 가능
                getGpsTracker();
            }
            else {
                // 거부된 권한이 있는 경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) // 사용자가 이전에 권한을 거절했다면 true 반환
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    // 권한 거부만 한 경우
                    Toast.makeText(MainActivity.this, "위치 권한이 거부되었습니다. 앱을 재실행하여 권한을 허용해주세요.", Toast.LENGTH_LONG).show();

                }else {
                    // "다시 묻지 않음"을 선택하고 권한을 거부한 경우
                    Toast.makeText(MainActivity.this, "위치 권한이 거부되었습니다. 설정(앱 정보)을 통해 권한을 허용해주세요. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission(){ //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)

            // 3.  위치 값을 가져올 수 있음

        } else {  // 2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... 주소, 지명 => 위도, 경도 좌표로 변호나
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
        /*
    public String findFacility(String address) {
        // 시설 사업자 리스트 배열 변수와 getGpsTracker() 함수의 리턴값을 비교하여 해당 주소의 시설 명부가 있는지 확인
        Intent intent = getIntent(); // 시설 사업자 명부 데이터 수신

        ArrayList<facilityList> list = (ArrayList<facilityList>) intent.getSerializableExtra("list");

        facilityList findAddress = null;

        for (facilityList f : list) {
            // GPS로 확인한 사용자의 위치 정보를 통해 해당 시설의 출입 명부가 존재하는지 찾기
            if (f.equals(address)) {
                findAddress = f;
                Toast.makeText(MainActivity.this, findAddress.name + " 명부 입장", Toast.LENGTH_LONG).show();
                facility = findAddress.name;

                // 다음 화면으로 최종 시설 정보 전달
                Intent secIntent = new Intent(this,.class);
                secIntent.putExtra("facility", facility);
                startActivity(secIntent);
                return true;

            } else {
                return false;
            }
        }
    }
 */


    /*
    //GPS 권한이 허용되었다면 해당 시설에 출입명부가 존재하는지 확인
    public String checkAccessList(String address) {
        // 해당 시설이 없다면 알림 띄우기
        // 이 기능은 나중에 사용자가 자동으로 연결 버튼을 누르는 경우, 최초 1번만 제공됨
        if (!findAddress(address)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("현재 위치에 해당하는\n" + "시설 명부가 존재하지 않습니다.");

            builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.setNeutralButton("재검색", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // 2차 검색 실패 시, 사용자에게 직접 시설명을 입력받음
                    if (!findAddress(address)) {
                        Toast.makeText(MainActivity.this, "재검색에 실패하였습니다.\n"+
                                   "시설명을 직접 입력해주세요.", Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);

                        builder2.setTitle("시설명 직접 입력");
                        builder2.setMessage("아래 예시와 같은 형식으로 시설명을 입력해주세요.\n" + "줄임말 X, 띄어쓰기 필수!");

                        EditText editText = (EditText)findViewById(R.id.editText);
                        //builder2.setView(editText);

                        builder2.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // 시설명 입력 받은 것을 토대로 재조사
                                if(!findAddress(editText.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "해당 시설에는 생성된 명부가 존재하지 않습니다.\n" +
                                            "시설 담당자에게 문의해주세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        builder2.create().show();
                    }
                }
            });
            builder.create().show();
        }
        return "";
    }
    */
}