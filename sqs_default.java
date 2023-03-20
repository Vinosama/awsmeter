import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

// Set the name of the SQS queue to publish messages to
String queueName = "YOUR_QUEUE_NAME";

// Set the path to the CSV file containing the messages
String filePath = "PATH_TO_YOUR_CSV_FILE";

// Create an SQS client with the DefaultAWSCredentialsProviderChain
AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

// Get the URL of the SQS queue
String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

// Read messages from the CSV file and publish them to the SQS queue
try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    String line;
    while ((line = br.readLine()) != null) {
        String messageBody = line.trim();
        SendMessageRequest request = new SendMessageRequest()
                                        .withQueueUrl(queueUrl)
                                        .withMessageBody(messageBody);
        sqs.sendMessage(request);
    }
} catch (Exception e) {
    log.error("Error reading messages from CSV file: {}", e.getMessage());
}
