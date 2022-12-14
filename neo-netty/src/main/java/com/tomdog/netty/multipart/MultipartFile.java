package com.tomdog.netty.multipart;

import lombok.*;

/**
 * 上传文件类
 *
 * @author Leo
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class MultipartFile {

    private String fileName;

    private String fileType;

    private byte[] fileData;

}
