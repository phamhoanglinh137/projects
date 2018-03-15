## Micro Services

### 1. Tech stack.
  ![Tech stack](micro.png)


   -  Spring Cloud : spring eureka discovery/registry, feign, hytrix, dashboard, spring cloud config
   -  Spring security, oauth2 using JWT.
   -  Apache kafka
   -  ELK (elasticsearch, logstask, kibana) for centralized logging
   -  Docker

### 2. How to run
   . . . updating soon.
### 3. Tech Summary

#### 3.1 oAuth2 :

 Detail Specification:
 * https://tools.ietf.org/html/rfc6749
 * https://auth0.com/docs/api-auth/which-oauth-flow-to-use

 #### Main components:
  - auth server
  - resource server
  - resource owner (user)
  - resource's user-agent(browser)
  - client application ( trusted application + untrusted html, javascript application )

 #### Grant type
  - authorization_code:
  - implicit
  - password
  - client_credential

### 4. Install sub-modules.
  #### 4.1 Install Elasticsearch
  https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

      healthcheck api:
      curl -XGET http://localhost:9200/_cluster/health?pretty=true

  #### 4.2 Install kibana and config
  https://www.elastic.co/guide/en/kibana/current/settings.html

  #### 4.3 Install logstash

  folder structure
  https://www.elastic.co/guide/en/logstash/current/dir-layout.html
