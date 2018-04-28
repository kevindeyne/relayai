package com.kevindeyne.tasker.service

import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component


@Component
open class EmailService(val mailSender : JavaMailSender, val environment: Environment) {

	//TODO add by who, add which project
	//TODO maybe use a login-once unique URL?
	//TODO what if user is already register with Relay?
	fun sendInvitationMail(email : String) {
		val username : String = "Kevin Deyne" //TODO
		val projectName : String = "Test project" //TODO

		if(!environment.activeProfiles.contains("dev")){
			val message = mailSender.createMimeMessage()
			message.subject = "RelayAI.io: Invitation to join"

			val helper = MimeMessageHelper(message, true)
			helper.setTo(email)
			helper.setFrom("noreply@relayai.io")
			helper.setText(buildTemplate(
					addParagraph("Hi there,") +
							addParagraph("You have been invited to '$projectName' by $username in the RelayAI project management tool. If you wish to join, click the button below.") +
							addButton("https://www.relayai.io/welcome#register?email=$email", "Join this project") +
							addParagraph("When you register a new account with us under this e-mail, it will automatically be coupled with the '$projectName' and require no further setup from you.") +
							addParagraph("Kind regards, ")
			), true)
			mailSender.send(message)
		}
	}

	fun sendActivationMail(key : String, email : String) {
		if(!environment.activeProfiles.contains("dev")){
			val message = mailSender.createMimeMessage()
			message.subject = "RelayAI.io: Please activate your account"

			val helper = MimeMessageHelper(message, true)
			helper.setTo(email)
			helper.setFrom("noreply@relayai.io")
			helper.setText(buildTemplate(
					addParagraph("Hi there,") +
					addParagraph("Welcome to RelayAI. We just need to activate your account before we can continue. The button below will bring you to a time-sensitive, unique URL.") +
					addButton("https://www.relayai.io/activation/$key", "Activate my account") +
					addParagraph("Kind regards, ")
			), true)
			mailSender.send(message)
		}
    }

	fun buildTemplate(content : String) : String {
		return "<!doctype html>" +
				"<html>" +
				"  <head>" +
				"    <meta name=\"viewport\" content=\"width=device-width\">" +
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
				"    <title>Email</title>    " +
				"  </head>" +
				"  <body class=\"\" style=\"background-color: #f6f6f6; font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">" +
				"    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background-color: #f6f6f6;\">" +
				"      <tr>" +
				"        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>" +
				"        <td class=\"container\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; Margin: 0 auto; max-width: 580px; padding: 10px; width: 580px;\">" +
				"          <div class=\"content\" style=\"box-sizing: border-box; display: block; Margin: 0 auto; max-width: 580px; padding: 10px;\">" +
				"            <!-- START CENTERED WHITE CONTAINER -->" +
				"            <span class=\"preheader\" style=\"color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;\">Welcome to RelayAI. We just need to activate your account before we can continue.</span>" +
				"            <table class=\"main\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background: #ffffff; border-radius: 3px;\">" +
				"              <!-- START MAIN CONTENT AREA -->" +
				"              <tr>" +
				"                <td class=\"wrapper\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;\">" +
				"                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">" +
				"                    <tr>" +
				"                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">$content</td>" +
				"                    </tr>" +
				"                  </table>" +
				"                </td>" +
				"              </tr>" +
				"            <!-- END MAIN CONTENT AREA -->" +
				"            </table>" +
				"            <!-- START FOOTER -->" +
				"            <div class=\"footer\" style=\"clear: both; Margin-top: 10px; text-align: center; width: 100%;\">" +
				"              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">" +
				"                <tr>" +
				"                  <td class=\"content-block\" style=\"font-family: sans-serif; vertical-align: top; padding-bottom: 10px; padding-top: 10px; font-size: 12px; color: #999999; text-align: center;\">" +
				"                    <span class=\"apple-link\" style=\"color: #999999; font-size: 12px; text-align: center;\">This is an automated message from RelayAI.io.</span>" +
				"                    <br> Don't like these emails? <a href=\"https://www.relayai.io/settings\" style=\"text-decoration: underline; color: #999999; font-size: 12px; text-align: center;\">Change your settings here</a>." +
				"                  </td>" +
				"                </tr>" +
				"              </table>" +
				"            </div>" +
				"            <!-- END FOOTER -->" +
				"          <!-- END CENTERED WHITE CONTAINER -->" +
				"          </div>" +
				"        </td>" +
				"        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>" +
				"      </tr>" +
				"    </table>" +
				"  </body>" +
				"</html>"
	}

	fun addButton(url : String, label : String) : String {
		return "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; box-sizing: border-box;\">" +
				"  <tbody>" +
				"    <tr>" +
				"      <td align=\"left\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; padding-bottom: 15px;\">" +
				"        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: auto;\">" +
				"          <tbody>" +
				"            <tr>" +
				"              <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; background-color: #3498db; border-radius: 5px; text-align: center;\"> <a href=\"$url\" target=\"_blank\" style=\"display: inline-block; color: #ffffff; background-color: #3498db; border: solid 1px #3498db; border-radius: 5px; box-sizing: border-box; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-transform: capitalize; border-color: #3498db;\">$label</a> </td>" +
				"            </tr>" +
				"          </tbody>" +
				"        </table>" +
				"      </td>" +
				"    </tr>" +
				"  </tbody>" +
				"</table>"
	}

	fun addParagraph(text : String) = "<p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">$text</p>"
}