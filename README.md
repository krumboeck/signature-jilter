# Mail signature milter software

## Note
THIS SOFTWARE COMES WITHOUT ANY WARRANTY!

## Description
**signature-filter** is a milter filter based on java which inserts template based signatures to mail messages. For this it searches the mail body for a mark (e.g. sig#template1#sig), which will be replaced with the template (according to the mark). It asks the configured Active Directory Server for the basic informations about the user who owns the "From" address (proxyAddress attribute). The it fills out the template and replace the mark with it.

Voila! Uniform mail signatures for your organization.

## License
* GPL Version 3 or later
* com.sendmail.jilter package use SENDMAIL OPEN SOURCE LICENSE

## Requirements
* Active directory (also a compatible LDAP server should work: Samba 4)
* MTA which supports MILTER (Sendmail, Postfix, ...)
* Java 8 or higher
* Maven (only for build)

## How to build
Build jar file with: `mvn install`

Build docker image with: `docker build -t signature-jilter .`

## Configure & run
Place an *application.properties* file into a subdirectory named *config* with following content:

```
# Server bind address
# server.address:0.0.0.0

# Server port
# server.port:8025

# Max number of server threads
# server.threads.max:10

# LDAP configuration
ldap.url=ldap://dc.example.com:389
ldap.base=dc=example,dc=com
ldap.userDN=cn=sign-jitler,ou=users,ou=department,dc=example,dc=com
ldap.password=verysecret

# Enable SSL/TLS or STARTTLS
# ssl.enable=true

# Path to all trusted certificates, if you don't want to use the system defaults.
# ssl.ca.file=/path/to/trusted-ca.crt

# Ignore hostname verification when using SSL/TLS
# ssl.ignoreHostname=false

# Directory of all template files
template.dir=/templates

# All domains where the signature should be inserted
template.domains=example.com
```

For all config options please look in the *application.properties* file in */src/main/resources*.

### Create template(s)

For every template you have to create a html and a txt file with the same name, which contains the template in the required format.

Example of txt template:

```
Best regards
*${ldap:displayName}*
${ldap:title}

${ldap:streetAddress}
${ldap:postalCode} ${ldap:l}

E: ${mailAddress}
T: ${ldap:telephoneNumber}
```

Example of html template:

```
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>signature template</title>
  </head>
  <body>
    Best regards
    <div class="moz-forward-container">
      <div class="moz-forward-container">
        <div class="moz-signature">
          <table style="margin-left: 6px;" border="0">
            <tbody>
              <tr>
                <td style="width: 120px;">
                    <img src="${ldap:thumbnailPhoto}" class=""></td>
                <td style="width: 380px;">
                  <p><span style="font-family: Arial, Helvetica, sans-serif; font-size: 15px; line-height: 1;">
                     <strong>${ldap:displayName}</strong><br>
                     <span style="color: #808080;">${ldap:title}</span></span></p>
                  <p><span style="font-family: Arial, Helvetica,
                                  sans-serif; font-size: 15px; line-height: 1;">
                       ${ldap:streetAddress}, ${ldap:postalCode} ${ldap:l}</span>
                     <span
                      style="font-family: Arial, Helvetica, sans-serif;
                      font-size: 15px; line-height: 1;"><br>
                      <a href="mailto:${mailAddress}"
                        moz-do-not-send="true">${mailAddress}</a><br>
                      ${ldap:telephoneNumber} </span></p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </body>
</html>
```

You can simple use the thunderbird composer to create a [file based signature](https://support.mozilla.org/en-US/kb/signatures#w_signatures-stored-in-files).

### Postfix configuration
Add the signature-jilter uri to your *smtpd_milters* in your postfix *main.cf* file:

```
smtpd_milters=inet:localhost:8025
```

### Configure mail client
Configure you mail client, so it simply adds a default signature like **sig#template1#sig#** to every mail.
