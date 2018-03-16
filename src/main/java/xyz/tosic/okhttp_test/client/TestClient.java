package xyz.tosic.okhttp_test.client;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;

import static org.eclipse.jetty.util.ssl.SslContextFactory.TRUST_ALL_CERTS;

public class TestClient {

  private static final Logger logger = LoggerFactory.getLogger("main");
  private static final OkHttpClient client = getUnsafeOkHttpClient();
  private static final byte[] TRASH = new byte[1024];

  public static void main(String[] args) {

    for (int j = 0; j < 100000; j++) {
      if (j % 1000 == 0) {
        System.out.println(j);
      }

      Request request = new Request.Builder().url("https://127.0.0.1:8443/").build();

      Call call = client.newCall(request);
      try (Response response = call.execute()) {
        if (!response.isSuccessful()) {
          logger.warn("response.status " + response.code());
        }

        response.body().source().read(TRASH);
        call.cancel();
      } catch (IOException ex) {
        logger.error("Error occurred on iteration [{}]", j, ex);
      }
    }
  }

  private static OkHttpClient getUnsafeOkHttpClient() {
    try {
      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager

      return new OkHttpClient.Builder()
          .sslSocketFactory(sslContext.getSocketFactory(),
              new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new java.security.cert.X509Certificate[]{};
                }
              })
          .hostnameVerifier((hostname, session) -> true)
          .build();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
