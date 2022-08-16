package filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import logger.LogUtil;

// This class can be used to customize rest assured logs but in this project I  used AllureRestAssured filter
// Usage:
// Filter logFilter = new CustomLogFilter(); RestAssured.filters(logFilter);
public class CustomLogFilter implements Filter {
		public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,FilterContext ctx) {
			Response response = ctx.next(requestSpec, responseSpec);
			StringBuilder requestBuilder = new StringBuilder();
			requestBuilder.append(requestSpec.getBaseUri());
			LogUtil.log(requestBuilder.toString());
			
			StringBuilder responseBuilder = new StringBuilder();
			responseBuilder.append(requestSpec.getBaseUri());
			LogUtil.log(responseBuilder.toString());
			return response;
		}
	}
