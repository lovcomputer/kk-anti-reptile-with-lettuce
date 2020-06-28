package cn.keking.anti_reptile.util;

import cn.keking.anti_reptile.module.VerifyImageDTO;
import com.wf.captcha.utils.CaptchaUtil;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

import java.io.ByteArrayOutputStream;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjh
 * @modify huanghz
 * @since 2019/7/16 11:05
 */
public class VerifyImageUtil {

    public VerifyImageDTO generateVerifyImg() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = CaptchaUtil.out(outputStream);
        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        String verifyId = UUID.randomUUID().toString();
        return new VerifyImageDTO(verifyId, null, base64Image, result);
    }

    /**
     * 保存验证码到Redis
     * @param verifyImage
     */
    public void saveVerifyCodeToRedis(VerifyImageDTO verifyImage) {
        stringRedisTemplate.boundValueOps(VERIFY_CODE_KEY + verifyImage.getVerifyId()).set(verifyImage.getResult(), 60, TimeUnit.SECONDS);
    }



    /**
     * 删除缓存
     * @param verifyId
     */
    public void deleteVerifyCodeFromRedis(String verifyId) {
        stringRedisTemplate.delete(VERIFY_CODE_KEY + verifyId);
    }

    /**
     * 获取缓存
     * @param verifyId
     * @return
     */
    public String getVerifyCodeFromRedis(String verifyId) {

        return stringRedisTemplate.boundValueOps(VERIFY_CODE_KEY + verifyId).get();

    }
    private static final String VERIFY_CODE_KEY = "kk-antireptile_verifycdoe_";

    @Resource
    private StringRedisTemplate stringRedisTemplate;
}
