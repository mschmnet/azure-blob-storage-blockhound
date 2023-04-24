package net.mschm.demos.blockhound.azure.blob;

import com.azure.storage.blob.BlobServiceAsyncClient;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AzureClient {

  private BlobServiceAsyncClient blobServiceAsyncClient;

  public AzureClient(BlobServiceAsyncClient blobServiceAsyncClient) {
    this.blobServiceAsyncClient = blobServiceAsyncClient;
  }

  public Mono<String> writeAndReadFromBlobFile(String content) {
    var blobContainerAsyncClient = blobServiceAsyncClient
        .getBlobContainerAsyncClient("testcontainer");
    var blockblobasyncclient = blobContainerAsyncClient
        .getBlobAsyncClient("testblob")
        .getBlockBlobAsyncClient();
    Flux<ByteBuffer> input = Flux.just(ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8)));
    var blockBlobItem = blockblobasyncclient.upload(input, content.length(), true);
    return blobContainerAsyncClient.createIfNotExists()
        .then(blockBlobItem.flatMapMany(b -> blockblobasyncclient.downloadStream())
            .map(ByteBuffer::array)
            .map(it -> new String(it, StandardCharsets.UTF_8))
            .reduce("", (a, b) -> a + b));
  }
}
