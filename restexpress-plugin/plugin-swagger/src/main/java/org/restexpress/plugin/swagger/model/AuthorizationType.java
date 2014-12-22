package org.restexpress.plugin.swagger.model;


//
//case class OAuth(
//  scopes: List[AuthorizationScope], 
//  grantTypes: List[GrantType]) extends AuthorizationType {
//  override def `type` = "oauth2"
//  override def getName: String = `type`
//}
//case class ApiKey(keyname: String, passAs: String = "header") extends AuthorizationType {
//  override def `type` = "apiKey"
//  override def getName: String = keyname
//}
//case class BasicAuth() extends AuthorizationType {
//  override def `type` = "basicAuth"
//  override def getName: String = `type`
//}
//
//trait GrantType {
//  def `type`: String
//}
//case class ImplicitGrant(
//  loginEndpoint: LoginEndpoint, 
//  tokenName: String) extends GrantType {
//  def `type` = "implicit"
//}
//case class AuthorizationCodeGrant(
//  tokenRequestEndpoint: TokenRequestEndpoint,
//  tokenEndpoint: TokenEndpoint) extends GrantType {
//  def `type` = "authorization_code"
//}
// to transform
//public enum AuthorizationTypes
//{
//	basicAuth, apiKey, oauth2
//}

public interface AuthorizationType {
	String type();
	String getName();
}
