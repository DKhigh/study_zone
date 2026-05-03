package weathercompare;

class WeatherData {
	// 모든 날씨 사이트의 공통 속성(최고/최저 기온, 강수확률, 습도, 풍속/풍향, 미세/초미세먼지)을 선언	
	// 전국8도(경기도, 강원도 등) + 제주도를 문자열로 선언
}

class Weather { 
	//WeatherData 객체를 변수 크기의 배열로 만드는 메소드 선언
}

class WeatherNuri extends Weather { 
	// 10 크기로 Weather 객체를 생성
	// 기상청의 특보(호우, 폭염, 강풍, 태풍)에 대한 정보를 선언 및 입출력하는 클래스 필요
}

interface MonthWeather { 
	// 30일 단위 예보 기능을 가진 사이트들이 반드시 만들어야 할 메서드 이름(구조)만 선언
}

class AccuWeather extends Weather implements MonthWeather { 
	// 30크기로 Weather 객체를 생성
	// 분 단위로 날씨를 표현하는 신속함에 특화됨, 이에 맞는 클래스 선언 필요
}

class WeatherChannel extends Weather implements MonthWeather { 
	// 30크기로 Weather 객체를 생성
	// 이슬점, 가시거리, 기압과 같은 기상 데이터에 특화됨, 이에 맞는 클래스 선언 필요
}

public class WeatherCompareMain {
	// WeatherNuri, AccuWeather, WeatherChannel 객체를 각각 생성
	// 생성된 객체들을 통해 날씨 데이터를 가져온 뒤 출력하는 메인 메소드
}
