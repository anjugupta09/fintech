package com.reloadly.filter;

import com.reloadly.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class CorrelationIdFilter extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String currentCorrId = httpServletRequest.getHeader(Constants.X_CORRELATION_ID);

        if (currentCorrId == null) {
            currentCorrId = UUID.randomUUID().toString();
            log.info("No correlationId found in Header. Generated : " + currentCorrId);
        } else {
            log.info("Found correlationId in Header : " + currentCorrId);
        }
        ThreadContext.put(Constants.X_CORRELATION_ID, currentCorrId);
        filterChain.doFilter(httpServletRequest, response);
    }
}
