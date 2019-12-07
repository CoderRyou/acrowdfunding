package junit.activiti;

import com.atguigu.atcrowdfunding.utils.DesUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SpringJavaMaliTest {
    public static void main(String[] args) throws Exception {
        //使用java程序发送邮件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/spring-*.xml");

        //邮件发送器，由spring框架提供
        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) applicationContext.getBean("sendMail");

        javaMailSender.setDefaultEncoding("UTF-8");
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);
        helper.setSubject("告白书");//邮件标题
        StringBuilder content = new StringBuilder();

        String param = "ILY";
        param = DesUtil.encrypt(param,"abcdefghijklmnopquvwxyz");

        content.append("<a href='http://www.atcrowdfunding.com/test/act.do?p="+param+"'>激活链接</a>");

        helper.setText(content.toString(),true);

        helper.setFrom("admin@crowdfunding.com");
        helper.setTo("test@crowdfunding.com");

        javaMailSender.send(mail);
    }
}
