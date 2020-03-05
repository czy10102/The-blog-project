package com.czy.common;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class GetUcloud {
    @Value("${ucloud.privatekey}")
    public String privateKey;    
    @Value("${ucloud.publickey}")
    public String publicKey;
    public String bucketName = "chengziyue";
    public String  upload(InputStream inputStream, String mimeType,String fileName){
        String generateFileName;
        String[] filePaths = fileName.split("\\.");
        if(filePaths.length > 1 ){
            generateFileName = UUID.randomUUID().toString().replace("-","") +"."+filePaths[filePaths.length - 1];
        }else{
            return null;
        }
        try {
            ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(publicKey,privateKey);
            ObjectConfig config = new ObjectConfig("cn-bj", "ufileos.com");
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generateFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {
                    }).execute();
            if(response !=null && response.getRetCode() ==0){
                // 得到图片地址 设置有效过期时间
                String url = UfileClient.object(OBJECT_AUTHORIZER,config).
                        getDownloadUrlFromPrivateBucket(generateFileName,bucketName,24 * 60 *60).createUrl();
                return url;
            }else{
                throw new CustomizeException(CustomizeErrorCode.PHOTO_UPLOADFALE);
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.PHOTO_UPLOADFALE);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.PHOTO_UPLOADFALE);
        }
    }
}
