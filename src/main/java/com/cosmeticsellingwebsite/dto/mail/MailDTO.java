package com.cosmeticsellingwebsite.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {
	private String from;
	private String to;
	private String subject;
	private String text;
}
