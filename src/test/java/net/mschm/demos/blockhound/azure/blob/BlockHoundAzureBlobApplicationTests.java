package net.mschm.demos.blockhound.azure.blob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.blockhound.BlockHound;
import reactor.test.StepVerifier;

@SpringBootTest
class BlockHoundAzureBlobApplicationTests {

  @Autowired
  private AzureClient azureClient;

  @BeforeEach
  public void setupBlockHound() {

    BlockHound.builder()
        .loadIntegrations()
        .blockingMethodCallback((method) -> {
          throw new Error("Disallowed blocking call: " + method.toString());
        })
//        .allowBlockingCallsInside("java.util.UUID", "randomUUID")
//        .allowBlockingCallsInside("com.azure.core.http.policy.RequestIdPolicy$1", "beforeSendingRequest")
        .allowBlockingCallsInside("java.util.ServiceLoader$LazyClassPathLookupIterator", "hasNext")
        .install();
  }

  @Test
  void testBlockHound() {
    StepVerifier.create(azureClient.writeAndReadFromBlobFile("Hello World"))
        .expectNext("Hello World")
        .verifyComplete();
  }

}
