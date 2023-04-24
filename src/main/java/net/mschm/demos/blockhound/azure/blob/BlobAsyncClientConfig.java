package net.mschm.demos.blockhound.azure.blob;

import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BlobAsyncClientConfig {


  private final Environment environment;

  public BlobAsyncClientConfig(Environment environment) {
    this.environment = environment;
  }

  @Bean
  public BlobServiceAsyncClient blobServiceAsyncClient() {
    return new BlobServiceClientBuilder().connectionString(Objects.requireNonNull(environment.getProperty("AZURE_STORAGE_ACCOUNT_CONNECTION_STRING")))
        .buildAsyncClient();
  }

}
