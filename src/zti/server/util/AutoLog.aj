package zti.server.util;

import java.time.LocalDateTime;

public aspect AutoLog {
	pointcut publicMethods() : execution (* zti.server..*(..));
	pointcut logObjectCalls() : execution (* Logger.*(..));
	pointcut autoLogObjectCalls() : execution (* AutoLog.*(..));
	pointcut dataObjectCalls() : execution (* zti.server.data..*(..));
	pointcut createElement() : execution (public * zti.server.util.Util.createElement(..));
	pointcut printException() : execution (public * zti.server.util.Util.printException(..));
	pointcut writeXmlToPrintWriter() : execution (public * zti.server.util.Util.writeXmlToPrintWriter(..));
	pointcut privateMethods() : execution (private * zti.server..*(..));
	
	pointcut loggableCalls() : publicMethods() && ! privateMethods() && ! logObjectCalls() && ! autoLogObjectCalls() && ! createElement() && ! printException() && ! writeXmlToPrintWriter() && ! dataObjectCalls();

	before() : loggableCalls(){
		Logger.Log(LocalDateTime.now(), "Entering method " + thisJoinPoint.getSignature().toString());
	}

	after() : loggableCalls(){
		Logger.Log(LocalDateTime.now(), "Exiting method " + thisJoinPoint.getSignature().toString());
	}
}