package adminPage;

import sportsFacArea.SelectedReserv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static login.Utility.*;

public class AdminPreReservedView {
    AdminRepository arp = new AdminRepository();


    public void showPreList() {
        List<SelectedReserv> userInfo = arp.loadReservationFile();
        List<SelectedReserv> approvedList = loadApprovedList();
        for (int i = 0; i < userInfo.size(); i++) {
            System.out.println(i + 1 + ". " + userInfo.get(i).info());
        }

        System.out.println("예약 승인 하려는 번호를 입력하세요");

        int preListNum = Integer.parseInt(input(">>"));
        SelectedReserv selectedReserv = userInfo.get(preListNum - 1);

        // 리스트에 해당 객체 삭제
        userInfo.remove(selectedReserv);
        makePreReservationSaveFile(userInfo);
//        System.out.println(userInfo);

        // 리스트에 추가
        approvedList.add(selectedReserv);
        // save함수 호출
        makeSelectedListSave(approvedList);

    }

    // 예약 대기 리스트를 save파일에 저장하는 메서드
    public void makePreReservationSaveFile(List<SelectedReserv> userInfo) {

        try (FileOutputStream fos
                     = new FileOutputStream(
                "BookingSystem/src/saveFile/reservationInfo.txt")) {

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userInfo);
//            for (SelectedReserv selectedReserv1 : userInfo) {
//                System.out.println(selectedReserv1 + "\n");
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {

        }
    }

    // 확인받은 예약을 저장하는 함수
    public void makeSelectedListSave(List<SelectedReserv> selectedList) { // 예약 리스트를 save파일에 저장하는 메서드
        try (FileOutputStream fos
                     = new FileOutputStream(
                "BookingSystem/src/saveFile/approvedList.txt")) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(selectedList);
//            System.out.println("성공");
//            for (SelectedReserv selectedReserv1 : selectedList) {
//                System.out.println(selectedReserv1 + "\n");
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
//            System.out.println("오류");
        }
    }

    public List<SelectedReserv> loadApprovedList() {
        List<SelectedReserv> selectedReservList = new ArrayList<>();
        try (FileInputStream fis
                     = new FileInputStream(
                "BookingSystem/src/saveFile/approvedList.txt")) {
            // 객체를 불러올 보조스트림
            ObjectInputStream ois = new ObjectInputStream(fis);
//             userInfo = (List<SelectedReserv>)ois.readObject();
//            ;
//            System.out.println(userInfo);
            System.out.println("여기");
            selectedReservList = (List<SelectedReserv>) ois.readObject();

        } catch (FileNotFoundException ex) {
//            System.out.println("파일이 존재하지 않습니다.");
//            ex.printStackTrace();
        } catch (IOException e) {
//            System.out.println("에러1");
//            e.printStackTrace();
        } catch (ClassNotFoundException e) {
//            System.out.println("에러2");
//            e.printStackTrace();
        }
        return selectedReservList;
    }


}



