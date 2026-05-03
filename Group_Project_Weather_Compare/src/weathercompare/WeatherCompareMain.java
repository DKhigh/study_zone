/* 구현해야 할 것들
 * 1. 달력 형식으로 한번에 해당 지역 날씨를 보기
 * 2. 특정 날자의 전국 지역 날씨를 보기
 * 3. 세부적인 데이터(습도, 강우확률 등)을 구현하기
 * 4. 날씨 데이터 설정을 미리 저장된 파일에서 불러오는 방식으로 구현하기
 * 5. 종합 날씨 비교 외 "세부 날씨 데이터 확인" 메뉴얼 추가하기
 * 6. 아큐웨더 및 웨더채널의 새부사항(분단위 날씨) 등 구현하기
 */

package weathercompare;

import java.util.Scanner;
import java.util.InputMismatchException; // 예외 처리를 위한 패키지 추가

class WeatherData { //기상청, 아큐웨더, 웨더채널 모두에서 제공하는 데이터 목록(현재 최대/최저 기온 및 날씨만 구현됨)
    double maxTemp;      
    double minTemp;      
    String condition;    
    int pop;             
    double windSpeed;    
    String windDirection;
    int pm10;            
    int pm25;            
}

class Weather { 
    String[] regions = {"경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"};
    
    WeatherData[][] forecastList;
    int maxDays; //기상청은 10일, 아큐웨더, 웨더채널은 28일까지 지원

    public Weather(int size) { //생성자
        this.maxDays = size;
        forecastList = new WeatherData[9][size]; 
        
        for (int region = 0; region < 9; region++) {  //지역, 날짜별 기본설정
            for (int day = 0; day < size; day++) { 
                forecastList[region][day] = new WeatherData();
                forecastList[region][day].condition = "설정 안 됨";
            }
        }
    }

    public void setRegionWeather(int regionIndex, int dayIndex, double max, double min, String cond) { //지역/날자별 날씨 설정
        if (dayIndex >= 0 && dayIndex < maxDays) {
            forecastList[regionIndex][dayIndex].maxTemp = max;
            forecastList[regionIndex][dayIndex].minTemp = min;
            forecastList[regionIndex][dayIndex].condition = cond;
        } else {
            System.out.println("오류: 최대 " + maxDays + "일까지만 예보를 설정할 수 있습니다.");
        }
    }

    public void printRegionWeatherByName(String providerName, String regionName, int dayIndex) { //지역/날자별 날씨 출력
        if (dayIndex >= maxDays) {
            System.out.println("[" + providerName + "] 최대 " + maxDays + "일까지만 지원하여 조회할 수 없습니다.");
            return;
        }
        
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].equals(regionName)) {
                System.out.println("[" + providerName + "] " + dayIndex + "일 후 상태: " 
                        + forecastList[i][dayIndex].condition 
                        + " / 최고: " + forecastList[i][dayIndex].maxTemp + "도"
                        + " / 최저: " + forecastList[i][dayIndex].minTemp + "도");
                return; 
            }
        }
    }
}

class WeatherNuri extends Weather { //기상청 날씨누리
    String alertInfo; 
    public WeatherNuri() { 
        super(10); 
    }
    public void setAlert(String alert) { this.alertInfo = alert; }
    public void printAlert() { System.out.println("[기상청 특보] " + alertInfo); }
}

// ★ 새롭게 추가된 중간 클래스 (28일 장기 예보 전용)
abstract class LongTermWeather extends Weather { //어큐웨더 / 웨더채널용 중간 클래스
    public LongTermWeather() {
        super(28); // 28일 고정 설정
    }

    // 자식 클래스(아큐웨더, 웨더채널)가 각자의 이름을 제공하도록 강제하는 추상 메서드
    abstract String getProviderName();

    public void print28DaysForecast(String regionName) { //28일 형식으로 날씨 예보 출력
        System.out.println("\n=== [" + getProviderName() + "] " + regionName + " 28일 장기 예보 ===");
        
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].equals(regionName)) {
                for (int d = 0; d < maxDays; d++) {
                    System.out.println(d + "일 후: " + forecastList[i][d].condition 
                            + " / 최고: " + forecastList[i][d].maxTemp + "도 / 최저: " + forecastList[i][d].minTemp + "도");
                }
                return;
            }
        }
        System.out.println("해당 지역을 찾을 수 없습니다.");
    }
}

class AccuWeather extends LongTermWeather { 
    String minuteCast; 
    public AccuWeather() { //Weather 생성자 호출
        super(); 
    }
    public void setMinuteCast(String info) { this.minuteCast = info; }
    public void printMinuteCast() { System.out.println("[아큐웨더 분 단위 예보] " + minuteCast); }
    
    @Override
    String getProviderName() { return "아큐웨더"; }
}

class WeatherChannel extends LongTermWeather { 
    double dewPoint;
    double visibility;
    int pressure;
    public WeatherChannel() { 
        super(); 
    }
    public void setExtraData(double dew, double vis, int press) {
        this.dewPoint = dew;
        this.visibility = vis;
        this.pressure = press;
    }
    public void printExtraData() {
        System.out.println("[웨더채널 특화 데이터] 이슬점: " + dewPoint + "도, 가시거리: " + visibility + "km");
    }
    
    @Override
    String getProviderName() { return "웨더채널"; }
}

public class WeatherCompareMain {

    public static void showCompareMenu(Scanner sc, WeatherNuri nuri, AccuWeather accu, WeatherChannel channel) {
        try {
            System.out.print("\n조회할 지역 이름을 입력하세요 (예: 강원도): ");
            String viewRegion = sc.next();
            
            System.out.print("조회할 날짜를 입력하세요 (0=오늘, 1=내일... 최대 27): ");
            int dayIdx = sc.nextInt();
            
            System.out.println("\n--- [" + viewRegion + "] " + dayIdx + "일 후 종합 날씨 비교 ---");
            nuri.printRegionWeatherByName("기상청 날씨누리", viewRegion, dayIdx);
            accu.printRegionWeatherByName("아큐웨더", viewRegion, dayIdx);
            channel.printRegionWeatherByName("웨더채널", viewRegion, dayIdx);
        } catch (InputMismatchException e) {
            System.out.println("오류: 날짜에는 숫자를 입력해야 합니다.");
            sc.nextLine(); // 잘못 입력된 쓰레기 값(버퍼) 비우기
        }
    }

    public static void showSettingMenu(Scanner sc, WeatherNuri nuri, AccuWeather accu, WeatherChannel channel) {
        try {
            System.out.println("\n[ 어떤 사이트의 날씨를 설정하시겠습니까? ]");
            System.out.println("1. 기상청(최대 10일) 2. 아큐웨더(최대 28일) 3. 웨더채널(최대 28일)");
            System.out.print("선택: ");
            int siteChoice = sc.nextInt();

            System.out.println("\n0:경기도 1:강원도 2:충북 3:충남 4:전북 5:전남 6:경북 7:경남 8:제주도");
            System.out.print("설정할 지역 번호를 입력하세요: ");
            int setIdx = sc.nextInt();
            
            System.out.print("설정할 날짜를 입력하세요 (0=오늘, 1=내일...): ");
            int dayIdx = sc.nextInt();
            
            System.out.print("최고 기온: ");
            double max = sc.nextDouble();
            System.out.print("최저 기온: ");
            double min = sc.nextDouble();
            System.out.print("날씨 (맑음/흐림/강우): ");
            String cond = sc.next();
            
            if (siteChoice == 1) {
                nuri.setRegionWeather(setIdx, dayIdx, max, min, cond);
                System.out.println("기상청 " + dayIdx + "일 후 데이터 설정 완료!");
            } else if (siteChoice == 2) {
                accu.setRegionWeather(setIdx, dayIdx, max, min, cond);
                System.out.println("아큐웨더 " + dayIdx + "일 후 데이터 설정 완료!");
            } else if (siteChoice == 3) {
                channel.setRegionWeather(setIdx, dayIdx, max, min, cond);
                System.out.println("웨더채널 " + dayIdx + "일 후 데이터 설정 완료!");
            } else {
                System.out.println("잘못된 사이트 번호입니다.");
            }
        } catch (InputMismatchException e) {
            System.out.println("오류: 입력 형식이 올바르지 않습니다. (기온, 날짜 등은 숫자로 입력해주세요)");
            sc.nextLine(); // 잘못 입력된 쓰레기 값(버퍼) 비우기
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        WeatherNuri nuri = new WeatherNuri();
        AccuWeather accu = new AccuWeather();
        WeatherChannel channel = new WeatherChannel();

        while (true) {
            System.out.println("\n=== 통합 날씨 관리 시스템 ===");
            System.out.println("1. 종합 날씨 비교 보기");
            System.out.println("2. 날씨 데이터 설정");
            System.out.println("3. 프로그램 종료");
            System.out.print("메뉴를 선택하세요: ");
            
            try {
                int menu = sc.nextInt();

                switch (menu) {
                    case 1:
                        showCompareMenu(sc, nuri, accu, channel);
                        break;
                    case 2:
                        showSettingMenu(sc, nuri, accu, channel);
                        break;
                    case 3:
                        System.out.println("프로그램을 종료합니다.");
                        sc.close();
                        return;
                    default:
                        System.out.println("잘못된 번호입니다. 다시 선택해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("오류: 메뉴 선택은 숫자로 입력해주세요.");
                sc.nextLine(); // 잘못 입력된 쓰레기 값(버퍼) 비우기
            }
        }
    }
}