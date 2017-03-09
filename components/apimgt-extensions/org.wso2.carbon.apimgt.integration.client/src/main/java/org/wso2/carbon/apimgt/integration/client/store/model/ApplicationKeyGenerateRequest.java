/**
 * WSO2 API Manager - Store
 * This document specifies a **RESTful API** for WSO2 **API Manager** - Store.  It is written with [swagger 2](http://swagger.io/). 
 *
 * OpenAPI spec version: 0.10.0
 * Contact: architecture@wso2.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.wso2.carbon.apimgt.integration.client.store.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * ApplicationKeyGenerateRequest
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-24T00:03:54.991+05:30")
public class ApplicationKeyGenerateRequest   {
  /**
   * Gets or Sets keyType
   */
  public enum KeyTypeEnum {
    PRODUCTION("PRODUCTION"),
    
    SANDBOX("SANDBOX");

    private String value;

    KeyTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static KeyTypeEnum fromValue(String text) {
      for (KeyTypeEnum b : KeyTypeEnum.values()) {
          if (String.valueOf(b.value).equals(text)) {
              return b;
          }
      }
      return null;
    }
  }

  @JsonProperty("keyType")
  private KeyTypeEnum keyType = null;

  @JsonProperty("validityTime")
  private String validityTime = null;

  @JsonProperty("callbackUrl")
  private String callbackUrl = null;

  @JsonProperty("accessAllowDomains")
  private List<String> accessAllowDomains = new ArrayList<String>();

  @JsonProperty("scopes")
  private List<String> scopes = new ArrayList<String>();

  public ApplicationKeyGenerateRequest keyType(KeyTypeEnum keyType) {
    this.keyType = keyType;
    return this;
  }

   /**
   * Get keyType
   * @return keyType
  **/
  @ApiModelProperty(example = "PRODUCTION", required = true, value = "")
  public KeyTypeEnum getKeyType() {
    return keyType;
  }

  public void setKeyType(KeyTypeEnum keyType) {
    this.keyType = keyType;
  }

  public ApplicationKeyGenerateRequest validityTime(String validityTime) {
    this.validityTime = validityTime;
    return this;
  }

   /**
   * Get validityTime
   * @return validityTime
  **/
  @ApiModelProperty(example = "3600", required = true, value = "")
  public String getValidityTime() {
    return validityTime;
  }

  public void setValidityTime(String validityTime) {
    this.validityTime = validityTime;
  }

  public ApplicationKeyGenerateRequest callbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    return this;
  }

   /**
   * Callback URL
   * @return callbackUrl
  **/
  @ApiModelProperty(example = "", value = "Callback URL")
  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public ApplicationKeyGenerateRequest accessAllowDomains(List<String> accessAllowDomains) {
    this.accessAllowDomains = accessAllowDomains;
    return this;
  }

  public ApplicationKeyGenerateRequest addAccessAllowDomainsItem(String accessAllowDomainsItem) {
    this.accessAllowDomains.add(accessAllowDomainsItem);
    return this;
  }

   /**
   * Allowed domains for the access token
   * @return accessAllowDomains
  **/
  @ApiModelProperty(example = "null", required = true, value = "Allowed domains for the access token")
  public List<String> getAccessAllowDomains() {
    return accessAllowDomains;
  }

  public void setAccessAllowDomains(List<String> accessAllowDomains) {
    this.accessAllowDomains = accessAllowDomains;
  }

  public ApplicationKeyGenerateRequest scopes(List<String> scopes) {
    this.scopes = scopes;
    return this;
  }

  public ApplicationKeyGenerateRequest addScopesItem(String scopesItem) {
    this.scopes.add(scopesItem);
    return this;
  }

   /**
   * Allowed scopes for the access token
   * @return scopes
  **/
  @ApiModelProperty(example = "null", value = "Allowed scopes for the access token")
  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationKeyGenerateRequest applicationKeyGenerateRequest = (ApplicationKeyGenerateRequest) o;
    return Objects.equals(this.keyType, applicationKeyGenerateRequest.keyType) &&
        Objects.equals(this.validityTime, applicationKeyGenerateRequest.validityTime) &&
        Objects.equals(this.callbackUrl, applicationKeyGenerateRequest.callbackUrl) &&
        Objects.equals(this.accessAllowDomains, applicationKeyGenerateRequest.accessAllowDomains) &&
        Objects.equals(this.scopes, applicationKeyGenerateRequest.scopes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyType, validityTime, callbackUrl, accessAllowDomains, scopes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationKeyGenerateRequest {\n");

    sb.append("    keyType: ").append(toIndentedString(keyType)).append("\n");
    sb.append("    validityTime: ").append(toIndentedString(validityTime)).append("\n");
    sb.append("    callbackUrl: ").append(toIndentedString(callbackUrl)).append("\n");
    sb.append("    accessAllowDomains: ").append(toIndentedString(accessAllowDomains)).append("\n");
    sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

