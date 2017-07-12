package cn.sinjinsong.common.client;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Created by SinjinSong on 2017/5/24.
 */
@Component("httpClientManager")
@ConfigurationProperties(prefix="client")
@PropertySource("classpath:client.properties")
@Getter
@Setter
public class HttpClientManager {
    private PoolingHttpClientConnectionManager manager = null;
    private Integer timeToLive;
    private Integer maxTotal;
    private Integer maxPerRoute;
    
    @PostConstruct
    public void init() {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry,null,null,null,timeToLive,TimeUnit.SECONDS);
        manager.setMaxTotal(maxTotal);
        manager.setDefaultMaxPerRoute(maxPerRoute);
    }

    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(manager)
                .build();

        return httpClient;
    }

    public InputStream openStream(String url) {
        try {
            CloseableHttpClient client = getHttpClient();
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpget);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean copyStream(String url,OutputStream os) {
        try {
            CloseableHttpClient client = getHttpClient();
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpget);
            response.getEntity().writeTo(os);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
}
