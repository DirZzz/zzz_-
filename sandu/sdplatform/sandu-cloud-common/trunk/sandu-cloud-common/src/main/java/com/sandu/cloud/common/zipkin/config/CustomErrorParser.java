package com.sandu.cloud.common.zipkin.config;

import brave.ErrorParser;

/** This is a simplified type used for parsing errors. It only allows annotations or tags. */
// This implementation works with SpanCustomizer and ScopedSpan which don't share a common interface
// yet both support tag and annotations.
public class CustomErrorParser extends ErrorParser{
 
  /**
   * Override to change what data from the error are parsed into the span modeling it. By default,
   * this tags "error" as the message or simple name of the type.
   */
  @Override
  protected void error(Throwable error, Object span) {
    /*String message = error.getMessage();
    if (message == null) message = error.getClass().getSimpleName();
    tag(span, "error", message);*/
		String message = error.getMessage();
		if (message == null) {
			message = error.getClass().getSimpleName();
			tag(span, "error", message);
		}else if(message.contains("SD_ERR_")) {
			tag(span, "biz_exception", message);
		}else {
			message = error.getStackTrace().toString();
			tag(span, "error", message);
		}
			
		
		
  }

 
}
