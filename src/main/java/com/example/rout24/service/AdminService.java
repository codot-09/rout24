package com.example.rout24.service;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.UnverifiedDriverInfoResponse;
import com.example.rout24.entity.DriverInfo;
import com.example.rout24.entity.enums.NotificationType;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.repository.DriverRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DriverRepository driverRepository;
    private final NotificationService notificationService;

    public ApiResponse<Page<UnverifiedDriverInfoResponse>> getUnverifiedRequests(LocalDate from, LocalDate to, String userId, Pageable pageable) {
        Page<DriverInfo> driverInfos = driverRepository.findUnverifiedDriverInfos(from, to, userId, pageable);
        
        Page<UnverifiedDriverInfoResponse> response = driverInfos.map(driverInfo -> {
            UnverifiedDriverInfoResponse unverifiedResponse = new UnverifiedDriverInfoResponse();
            unverifiedResponse.setId(driverInfo.getId());
            unverifiedResponse.setDriverName(driverInfo.getDriver().getFullName());
            unverifiedResponse.setDriverContact(driverInfo.getDriver().getChatId());
            unverifiedResponse.setDriverLicense(driverInfo.getDriverLicense());
            unverifiedResponse.setPassportId(driverInfo.getPassportId());
            unverifiedResponse.setBirthDate(driverInfo.getBirthDate());
            unverifiedResponse.setGender(driverInfo.getGender() != null ? driverInfo.getGender().name() : null);
            return unverifiedResponse;
        });
        
        return ApiResponse.success(response);
    }

    public ApiResponse<String> confirmRequest(UUID requestId,boolean status){
        DriverInfo info = driverRepository.findById(requestId)
            .orElseThrow(() -> new DataNotFoundException("Malumot topilmadi"));

        info.setVerified(status);

        driverRepository.save(info);

        notificationService.sendNotification(info.getDriver(), "Malumotlar tasdiqlandi !", "Malumotlaringiz tasdiqlandi va profil faollashtirildi ! Endi mashina qo'shing va faoliyatni boshlang",NotificationType.SYSTEM);

        return ApiResponse.success(null,"So'rov tasdiqlandi");
    }

}
