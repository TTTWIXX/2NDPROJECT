package sportsFacArea;

import adminPage.AdminRepository;
import login.UserInfo;
import memberShipUserSystem.MemberShipUserInfo;
import memberShipUserSystem.MemberShipUserView;
import mypage.MyPageView;
import sportsFacArea.sportrentlist.BasketRentList;
import sportsFacArea.sportrentlist.SoccerRentList;
import sportsFacArea.sportrentlist.SwimRentList;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static login.Utility.input;

public class BookingView {
    MemberShipUserView memberView;

    static SportAreaRepository repository;
    static SportBooking booking;
    static DateList date;
    static UserInfo info;
    static SelectedReserv reserv;
    static SoccerRentList soccerRentList;
    static BasketRentList basketRentList;
    static SwimRentList swimRentList;
    static Calendar calendar;

    static MemberShipUserInfo myInfo;
    static AdminRepository adminRepository;
    static MyPageView myPageView;

    static {
        repository = new SportAreaRepository();
        adminRepository = new AdminRepository();
        booking = new SportBooking();
        date = new DateList();
        info = new UserInfo();
        reserv = new SelectedReserv();
        soccerRentList = new SoccerRentList();
        myInfo = new MemberShipUserInfo();
        basketRentList = new BasketRentList();
        swimRentList = new SwimRentList();
        calendar = Calendar.getInstance();
        myPageView = new MyPageView();
    }

    public void areaStart() { // 지역 정하기
        System.out.println("\n        [ 지역 예약 ]");
        memberView = new MemberShipUserView();
        memberView.getUserArea();
        sportStart();
    }



    public void sportStart() { // 스포츠 종목 정하기
        System.out.println("\n        [ 구장 예약 ]");
        System.out.println("1. 축구장");
        System.out.println("2. 농구장");
        System.out.println("3. 수영장");
        String sportNum = input("\n# 예약할 구장 선택하기>> ");
        switch (sportNum) {
            case "1":
                reserv.setUserSport("축구장");
                bookingFac(); // 예약 날짜 정하기
                break;
            case "2":
                reserv.setUserSport("농구장");
                bookingFac(); // 예약 날짜 정하기
                break;
            case "3":
                reserv.setUserSport("수영장");
                bookingFac(); // 예약 날짜 정하기
                break;
            default:
                System.out.println("잘못된 입력입니다");
                sportStart();
        }
    }

    public void bookingFac() { // 예약 날짜 정하기 메서드
        System.out.println("# 이용기간 5월");

        String inputDay = input("[ 1일 ~ 31일 ] 입력 >> ");
        try {
            if (Integer.parseInt(inputDay) > 31 || Integer.parseInt(inputDay) < 1) {
                System.out.println("다시 입력하세요");
                bookingFac(); // 다시 예약 날짜 정하기
            }
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다");
            bookingFac();
        }
        denyDate(inputDay);

        int inputTime = timeInterval(inputDay); // 예약 시간대 정하기 메서드

        boolean isRent = rentStuff();
        boolean isParking = parkCoupon();

        booking = new SportBooking(inputDay, inputTime, isRent, isParking);

        insertWaitList(inputDay, inputTime, isRent, isParking); // 예약 정보들 리스트에 담아두는 메서드
        reservationInfo(); // 예약 정보 출력
        confirmRes(); // 마지막으로 예약 할 것인지 물어보는 메서드
    }

    public void denyDate(String inputDay) { // 예약 대기,완료된 날짜는 선택 못하게 하는 메서드
        if (myPageView.loadApprovedList() != null) {
            List<SelectedReserv> selectedReservList = myPageView.loadApprovedList(); // 예약완료된 세이브파일 불러옴
            for (SelectedReserv selectedReserv : selectedReservList) {
                if (selectedReserv.getUserName().equals(myInfo.getUserName())
                        && selectedReserv.getUserPlace().equals(reserv.getUserPlace())
                        && selectedReserv.getUserSport().equals(reserv.getUserSport())
                        && selectedReserv.getUserDate().equals(inputDay)) {
                    System.out.printf("\n%s님은 이미 5월 %s일을 예약하셨습니다!\n\n", myInfo.getUserName(), inputDay);
                    bookingFac(); // 다시 예약 날짜 정하기
                }
            }
        }
        if (adminRepository.loadReservationFile() != null) {
            List<SelectedReserv> reservationFile = adminRepository.loadReservationFile(); // 예약 대기 세이브파일
            for (SelectedReserv selectedReserv : reservationFile) {
                if (selectedReserv.getUserName().equals(myInfo.getUserName())
                        && selectedReserv.getUserPlace().equals(reserv.getUserPlace())
                        && selectedReserv.getUserSport().equals(reserv.getUserSport())
                        && selectedReserv.getUserDate().equals(inputDay)) {
                    System.out.printf("\n%s님은 이미 5월 %s일을 예약하셨습니다!\n\n", myInfo.getUserName(), inputDay);
                    bookingFac();// 다시 예약 날짜 정하기
                }
            }
        }

    }

    private void insertWaitList(String inputDay, int inputTime, boolean isRent, boolean isParking) { // 예약 대기리스트에 담는 메서드
        reserv.setUserDate(inputDay);
        reserv.setUserTimeIndex(inputTime);
        reserv.setRent(isRent);
        reserv.setParking(isParking);
        reserv.setUserTime(date.callMap().get(booking.getBookingDay()).dateList.get(booking.getTimeIndex() - 1));
    }

    public int timeInterval(String inputDay) { // 예약 시간 정하기 메서드
        System.out.println("\n5월 " + inputDay + "일 운영시간 [ 10:00 ~ 22:00 ] 2시간 단위");
        List<SelectedReserv> selectedReservList = myPageView.loadApprovedList(); // 예약완료된 세이브파일 불러옴
        Map<String, TimeList> timeListMap = date.callMap(); // 날짜를 map에 저장
        TimeList timeList = timeListMap.get(inputDay); // 해당 날짜의 시간대 불러오기
        timeList.inform(); // 예약 가능한 시간대 출력
        int inputTime = 0;
        try {
            inputTime = Integer.parseInt(input("\n# 예약할 시간을 번호로 입력 >> "));
            if (inputTime > 6 || inputTime < 1) {
                System.out.println("잘못된 입력입니다");
                timeInterval(inputDay); // 다시 예약 시간 입력받기
            }
            denyTime(inputTime, inputDay);
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다");
            timeInterval(inputDay); // 다시 예약 시간 입력받기
        }
        return inputTime;
    }

    public void denyTime(int inputTime, String inputDay) { // 예약 완료된 시간대는 다시 입력받게하는 메서드
        List<SelectedReserv> selectedReservList = myPageView.loadApprovedList(); // 예약완료된 세이브파일 불러옴
        for (SelectedReserv selectedReserv : selectedReservList) {
            if (selectedReserv.getUserPlace().equals(reserv.getUserPlace())
                    && selectedReserv.getUserSport().equals(reserv.getUserSport())
                    && selectedReserv.getUserDate().equals(inputDay)
                    && selectedReserv.getUserTimeIndex() == inputTime) {
                System.out.printf("이미 5월 %s일 %s 시간대는 예약완료상태입니다!\n\n", inputDay, new TimeList().dateList.get(inputTime - 1));
                timeInterval(inputDay); // 다시 예약 시간대 입력받기
            }
        }

    }


    public int ageDisCount() { // 학생 할인 기준
        int userAge = 2023 - Integer.parseInt(myInfo.getUserAge().substring(0, 4));
        if (userAge < 25) return 10;
        return 0;
    }

    public int placeDisCount() { // 지역 할인 기준
        if (myInfo.getUserArea().equals(reserv.getUserPlace())) return 10;
        return 0;
    }

    public void showTotalPrice(int allTotal) { // 모든 할인 계산하는 메서드
        int academyDiscount = 0;
        switch (input("중앙정보처리학원을 다니십니까? [y/n] ").toUpperCase().charAt(0)) {
            case 'Y':
                System.out.println("추가 20%할인 적용되었습니다!");
                academyDiscount = (allTotal / 10) * 2;
                break;
            case 'N':
                break;
            default:
                System.out.println("잘못된 입력입니다");
                showTotalPrice(allTotal);
        }
        int ageDiscount = 0;
        int placeDiscount = 0;
        if (ageDisCount() != 0) ageDiscount = allTotal / ageDisCount();
        if (placeDisCount() != 0) placeDiscount = allTotal / placeDisCount();

        int total = allTotal - (ageDiscount + placeDiscount + academyDiscount);

        System.out.println("학생 할인 : " + ageDisCount() + "%");
        System.out.println("지역 할인 : " + placeDisCount() + "%");

        reserv.setUserTotal(total); // 총 금액 회원 정보로 넘기기
        System.out.printf("결제할 총 금액 : %d원\n", reserv.getUserTotal());
    }

    public void reservationInfo() { // 예약 정보들 출력 메서드
        System.out.println("\n      [ 예약 정보 확인 ]");
        reserv.setUserName(myInfo.getUserName());
        System.out.println(reserv.inform());
        System.out.println("\n      [ 가 격 표 ]");
        int allTotal = 0;
        switch (reserv.getUserSport()) {
            case "축구장":
//                count = soccerRentList.rentCount(); // 대여한 물품의 개수 출력 메서드
                allTotal = soccerRentList.rentTotal(); // 대여한 물품들의 가격 계산 메서드
                System.out.println("구장 비용은 100,000원입니다!");
                System.out.println(soccerRentList.allInfo());
                System.out.printf("대여한 물품의 총 가격 : %d원\n", soccerRentList.rentCount());
                break;
            case "농구장":
//                count = basketRentList.rentCount(); // 대여한 물품의 개수 출력 메서드
                allTotal = basketRentList.rentTotal(); // 대여한 물품들의 가격 계산 메서드
                System.out.println("구장 비용은 100,000원입니다!");
                System.out.println(basketRentList.allInfo());
                System.out.printf("대여한 물품의 총 가격 : %d원\n", basketRentList.rentCount());
                break;
            case "수영장":
//                count = swimRentList.rentCount(); // 대여한 물품의 개수 출력 메서드
                allTotal = swimRentList.rentTotal(); // 대여한 물품들의 가격 계산 메서드
                System.out.println("구장 비용은 20,000원입니다!");
                System.out.println(swimRentList.allInfo());
                System.out.printf("대여한 물품의 총 가격 : %d원\n", swimRentList.rentCount());
                break;
        }
        showTotalPrice(allTotal);
    }


    public boolean rentStuff() { // 대여물품 렌트 여부
        String inputRent = input("# 대여물품을 선택하시겠습니까? [y/n] ");
        boolean isRent = false;
        switch (inputRent.toUpperCase().charAt(0)) {
            case 'Y':
                isRent = true;
                if (reserv.getUserSport().equals("축구장"))
                    soccerRentList.soccerRentList(); // 축구장 렌트 물품 보여주는 메서드
                else if (reserv.getUserSport().equals("농구장"))
                    basketRentList.basketRentList(); // 농구장 렌트 물품 보여주는 메서드
                else if (reserv.getUserSport().equals("수영장"))
                    swimRentList.swimRentList(); // 수영장 렌트 물품 보여주는 메서드
                break;
            case 'N':
                break;
            default:
                System.out.println("잘못된 입력입니다");
                rentStuff();
        }
        return isRent;
    }


    public boolean parkCoupon() { // 주차 유무 확인 후 쿠폰 지금
        String inputParking = input("# 주차 쿠폰을 발행하시겠습니까? [y/n] ");
        boolean isParking = false;
        switch (inputParking.toUpperCase().charAt(0)) {
            case 'Y':
                isParking = true;
                break;
            case 'N':
                break;
            default:
                System.out.println("잘못된 입력입니다");
                parkCoupon();
        }
        return isParking;
    }

    public void confirmRes() { // 마지막으로 예약을 할 것인지 물어보는 메서드
        String inputRes = input("예약 하시겠습니까? [y/n] ");
        switch (inputRes.toUpperCase().charAt(0)) {
            case 'Y':
                makeSaveFile();
                myPageView.showLoginSuccess();
                break;
            case 'N':
                myPageView.showLoginSuccess();
                break;
            default:
                System.out.println("잘못된 입력입니다");
                confirmRes();
        }

    }


    public static void loginInfo(MemberShipUserInfo userInfo) {
        myInfo = userInfo;
    }

    public void makeSaveFile() { // 예약 리스트를 save파일에 저장하는 메서드
        List<SelectedReserv> reservationFile = new ArrayList<>();
        if (adminRepository.loadReservationFile() != null) {
            reservationFile = adminRepository.loadReservationFile();
        }
        try (FileOutputStream fos
                     = new FileOutputStream(
                "BookingSystem/src/saveFile/reservationInfo.txt")) {
            SelectedReserv selectedReserv = new SelectedReserv(reserv.getUserName(), reserv.getUserSport(),
                    reserv.getUserPlace(), reserv.getUserDate(), reserv.getUserTime(),
                    reserv.getUserTimeIndex(), reserv.getUserTotal(), reserv.isRent(), reserv.isParking());
            reservationFile.add(selectedReserv);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(reservationFile);
//            System.out.println("성공");
//            for (SelectedReserv selectedReserv1 : reservationFile) {
//                System.out.println(selectedReserv1 + "\n");
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
//            e.printStackTrace();
//            System.out.println("오류");
        }
    }

}


