package com.example.rout24.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    private final CloudService cloudService;

    public String generateQRCodeUrl(Integer billingNumber) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    billingNumber.toString(),
                    BarcodeFormat.QR_CODE,
                    350,
                    350
            );

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
            byte[] pngBytes = stream.toByteArray();

            MockMultipartFile file = new MockMultipartFile(
                    "qr",
                    "qr_" + billingNumber + ".png",
                    "image/png",
                    pngBytes
            );

            return cloudService.uploadImage(file);

        } catch (Exception e) {
            throw new RuntimeException("QR code yaratishda xatolik", e);
        }
    }

    public Integer parseQRCode(MultipartFile file) {
        try {
            BufferedImage image = javax.imageio.ImageIO.read(file.getInputStream());
            if (image == null) throw new IllegalArgumentException("Fayl rasm emas yoki o‘qib bo‘lmadi");

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);

            return Integer.parseInt(result.getText().trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("QR code dan olingan matn butun son emas", e);
        } catch (Exception e) {
            throw new RuntimeException("QR code o‘qishda xatolik", e);
        }
    }

    private MultipartFile toMultipart(byte[] bytes, String name) {
        return new MockMultipartFile(
                name,
                name + ".png",
                "image/png",
                bytes
        );
    }
}
