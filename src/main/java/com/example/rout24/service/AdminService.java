package com.example.rout24.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.rout24.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.UnverifiedDriverInfoResponse;
import com.example.rout24.entity.DriverInfo;
import com.example.rout24.entity.enums.NotificationType;
import com.example.rout24.entity.enums.RequestStatus;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.repository.DriverRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DriverRepository driverRepository;
    private final NotificationService notificationService;

    public ApiResponse<PagedResponse<UnverifiedDriverInfoResponse>> getUnverifiedRequests(
            LocalDate from, LocalDate to, String userId, Pageable pageable) {

        Page<DriverInfo> driverInfos = driverRepository.findUnverifiedDriverInfos(from, to, userId, pageable);

        List<UnverifiedDriverInfoResponse> content = driverInfos.getContent().stream().map(driverInfo -> {
            UnverifiedDriverInfoResponse response = new UnverifiedDriverInfoResponse();
            response.setId(driverInfo.getId());
            response.setDriverName(driverInfo.getDriver().getFullName());
            response.setDriverContact(driverInfo.getDriver().getChatId());
            response.setDriverLicense(driverInfo.getDriverLicense());
            response.setPassportId(driverInfo.getPassportId());
            response.setBirthDate(driverInfo.getBirthDate());
            response.setUsername(driverInfo.getDriver().getUsername());
            response.setPhone(driverInfo.getPhoneNumber());
            response.setGender(driverInfo.getGender() != null ? driverInfo.getGender().name() : null);
            return response;
        }).toList();

        PagedResponse<UnverifiedDriverInfoResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(driverInfos.getNumber());
        pagedResponse.setPageSize(driverInfos.getSize());
        pagedResponse.setTotalElements(driverInfos.getTotalElements());
        pagedResponse.setTotalPages(driverInfos.getTotalPages());
        pagedResponse.setLast(driverInfos.isLast());

        return ApiResponse.success(pagedResponse);
    }

    public ApiResponse<String> confirmRequest(UUID requestId,RequestStatus status){
        DriverInfo info = driverRepository.findById(requestId)
            .orElseThrow(() -> new DataNotFoundException("Malumot topilmadi"));

        info.setStatus(status);

        driverRepository.save(info);

        notificationService.sendNotification(info.getDriver(), "Malumotlar tasdiqlandi !", "Malumotlaringiz tasdiqlandi va profil faollashtirildi ! Endi mashina qo'shing va faoliyatni boshlang",NotificationType.SYSTEM);

        return ApiResponse.success(null,"So'rov tasdiqlandi");
    }

}
