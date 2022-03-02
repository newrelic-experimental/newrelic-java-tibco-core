# Disabling the Product instrumentation for JMS
   
1.  Edit newrelic.yml
2.  Find the following lines:
----

      class_transformer:   
         # This instrumentation reports the name of the user principal returned from   
         # HttpServletRequest.getUserPrincipal() when servlets and filters are invoked.   
         com.newrelic.instrumentation.servlet-user:   
           enabled: false    

         com.newrelic.instrumentation.spring-aop-2:   
           enabled: false    

----
   
3. Using the same spacing as the last two lines, add the folloinwg after the last two lines    
----

         com.newrelic.instrumentation.jms-1.1:   
           enabled: false    

----
4. Save newrelic.yml
   
