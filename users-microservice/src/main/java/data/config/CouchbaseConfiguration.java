package data.config;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.cache.CouchbaseCache;
import org.springframework.data.couchbase.cache.CouchbaseCacheManager;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseFactoryBean;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kennybastani on 7/31/15.
 */
@Configuration
public class CouchbaseConfiguration extends AbstractCouchbaseConfiguration {
    @Value("${couchbase.cluster.bucket:default}")
    private String bucketName;

    @Value("${couchbase.cluster.password:}")
    private String password;

    @Value("${couchbase.cluster.ip:192.168.59.103}")
    private String ip;

    @Value("${couchbase.cluster.ttl:90}")
    private Integer timeout;

    @Override
    protected List<String> bootstrapHosts() {
        return Arrays.asList(ip);
    }

    @Override
    protected String getBucketName() {
        return "default";
    }

    @Override
    protected String getBucketPassword() {
        return password;
    }

    @Bean(name = "couchbaseCacheManager")
    public CouchbaseCacheManager couchbaseCacheManager() {
        HashMap<String, CouchbaseClient> instances = new HashMap<>();

        try {
            instances.put("cache", couchbaseFactoryBean().getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Integer> ttl = new HashMap<>();
        ttl.put("cache", timeout);

        return new CouchbaseCacheManager(instances, ttl);
    }

    @Bean
    public CouchbaseFactoryBean couchbaseFactoryBean() {
        CouchbaseFactoryBean couchbaseFactoryBean = new CouchbaseFactoryBean();
        couchbaseFactoryBean.setOpTimeout(timeout);
        couchbaseFactoryBean.setBucket(bucketName);
        couchbaseFactoryBean.setPassword(password);

        couchbaseFactoryBean.setHost(ip);

        return couchbaseFactoryBean;
    }
}
