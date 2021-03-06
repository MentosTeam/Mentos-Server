package MentosServer.mentos.utils.fcm;

import MentosServer.mentos.config.BaseException;
import MentosServer.mentos.config.BaseResponseStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static MentosServer.mentos.config.BaseResponseStatus.FCM_SEND_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/mentos-4adeb/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(List<String> targetToken, String title, String body, int flag) throws BaseException {
        AtomicBoolean fcmPushSuccess = new AtomicBoolean(true);
        //log.info("sendMessageTo method");
        targetToken.forEach(i-> {
            try {
            String message = makeMessage(i, title, body, flag);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message,
                    MediaType.get("application/json; charset=utf-8"));
            Request request = null;
                request = new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                        .build();

                Response response = client.newCall(request).execute();

                //log.info(response.body().string());
            } catch (IOException e) {
                fcmPushSuccess.set(false);
            }
        });
        if(!fcmPushSuccess.get()){
            throw new BaseException(FCM_SEND_ERROR);
        }
    }
    private String makeMessage(String targetToken, String title, String body,int flag) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .data(FcmMessage.Data.builder()
                                .title(title)
                                .body(body)
                                .receiverFlag(Integer.toString(flag))
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}

