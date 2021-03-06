package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.smsserver.services.models.mobileoriginated.Message;

import javassist.NotFoundException;

@Stateless
public class Pithia {

    @Resource(lookup = "jdbc/pithia")
    DataSource pithia;

    public String[] queryPithia(Message message, String authenticator, String[] body, int extra) throws Exception {

        String[] replacements = new String[message.getNumberOfReplacements()];

        try (Connection conn = pithia.getConnection(); PreparedStatement stmt = conn.prepareStatement(message.getQuery());) {

            stmt.setString(1, authenticator);
            for (int i = 1; i < message.getQueryParams(); i++)// ksekinaei apo 1
            {
                if (message.getLikePosition()-1 == i) {										
                    String titleLike = "%";
                    for (int j = i+extra; j <= body.length; j++) {
                        titleLike += body[j-1] + "%";
                    }
                    stmt.setString(i + 1, titleLike);
                } else {
                    stmt.setString(i + 1, body[i - 1 + extra]);

                }

            }

            // -1 gia na paei sto 0 . extra an iparxei secondary
            System.out.println(authenticator);
            try (ResultSet rs = stmt.executeQuery();) {
                if (!rs.next()) {
                    throw new NotFoundException("query");
                } else {
                    for (int i = 0; i < replacements.length; i++) {
                        replacements[i] = rs.getString(i + 1);
                    }
                }
            }
        }
        return replacements;

    }

}
