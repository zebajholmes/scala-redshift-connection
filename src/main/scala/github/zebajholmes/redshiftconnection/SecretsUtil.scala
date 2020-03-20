package github.zebajholmes.redshiftconnection

import com.amazonaws.services.secretsmanager.model.{DecryptionFailureException,
  InternalServiceErrorException, InvalidParameterException, ResourceNotFoundException}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest
import com.amazonaws.services.secretsmanager.model.InvalidRequestException


object SecretsUtil {

  case class RsCreds(host: String, user: String, database: String, port: String, password: String)

  def getRsCreds(secretName: String, region: String = "us-east-1"): RsCreds = {

    parseSecret(getSecretString(secretName, region))

  }


  def parseSecret(rawSecret: String): RsCreds =  {

    val find = (l: Array[Array[String]], cred: String) => l.filter(_.head == cred).head.last

    val rs: String = rawSecret.substring(1, rawSecret.length-1).replaceAll(""""""", "")
    val l: Array[Array[String]] = rs.split(",").map(i => i.split(":"))

    RsCreds(find(l, "host"), find(l, "username"), find(l, "dbClusterIdentifier") , find(l, "port"), find(l, "password") )

  }


  def getSecretString(secretName: String, region: String): String = {

    val endpoint = s"secretsmanager.${region}.amazonaws.com"

    val config = new AwsClientBuilder.EndpointConfiguration(endpoint, region)
    val clientBuilder = AWSSecretsManagerClientBuilder.standard()
    clientBuilder.setEndpointConfiguration(config)

    val client = clientBuilder.build
    val getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)
      .withVersionStage("AWSCURRENT")
    var secret: Option[String] = None

    try {

      val getSecretValueResult = client.getSecretValue(getSecretValueRequest)
      secret = Some(getSecretValueResult.getSecretString)

    } catch {

      case e: ResourceNotFoundException =>
        System.out.println("The requested secret " + secretName + " was not found")
      case e: InvalidRequestException =>
        System.out.println("The request was invalid due to: " + e.getMessage)
      case e: InvalidParameterException =>
        System.out.println("The request had invalid params: " + e.getMessage)
      case e: DecryptionFailureException =>
        System.out.println("Unable to decrypt info: " + e.getMessage)
      case e: InternalServiceErrorException =>
        System.out.println("Internal error: " + e.getMessage)

    } finally {

      client.shutdown()

    }

    secret.get

  }

}
