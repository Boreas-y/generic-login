generic-login
=============
Generic-login is a simple login component based on Spring Security, providing several common features:
+ Minimize configurations and configure by .properties file.
+ Provide a default captcha generator. Also can user custom captcha generator by implements some interfaces.
+ Enable saving more user information in session when logging success. Provide a PasswordProtector interface for security.
+ Enable Ajax login.

##Q&A
+ How to use Ajax login?
  - Need a non-empty parameter named ajax, such as "ajax=√&username=xxx&password=xxx"
  - return a JSON object {result:resultCode, errCount:errorCount, msg: errorMessage}, resultCode 1 for success and 0 for failure.
    What's more, errorMessage is in Simplified Chinese by default and can be configured.

+ How to use captcha?
  - The URL fetching captcha is /captcha, need a parameter which indicates usage such as /captcha?login. This parameter should be defined in properties file
  - The URL validating captcha is /captcha?check=**&captcha=xxxx. The check parameter is depend on usage defined in fetching url.
  - Validation will return a JSON object {result:resultCode,errCount:errorCount}, resultCode 1 for success, 0 for expired, -1 for failure.
