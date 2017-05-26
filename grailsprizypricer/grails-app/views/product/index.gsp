
<%@ page import="com.grails.practical.Product" %>
<!DOCTYPE html>
	<html>
		<head>
				<meta name="layout" content="main">
				<g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
				<title><g:message code="default.list.label" args="[entityName]" /></title>
						<link rel="stylesheet" href="${resource(dir: 'css', file: '_grails.css')}" type="text/css"/>
						<g:javascript src="search.js"></g:javascript>
						<style>.filteredList{color:gray;}</style>
		
		</head>
		<body>
			<a href="#list-product" class="skip" tabindex="-1"><g:message
					code="default.link.skip.label" default="Skip to content&hellip;" /></a>
			<div class="nav" role="navigation">
				<ul>
					<li><a class="home" href="${createLink(uri: '/')}"><g:message
								code="default.home.label" /></a></li>
					<li><g:link class="create" action="create">
							<g:message code="default.new.label" args="[entityName]" />
						</g:link></li>
				</ul>
			</div>
			<div id="list-product" class="content scaffold-list" role="main" style="position: sticky;">
				<h1>
					<g:message code="default.list.label" args="[entityName]" />
				</h1>
				<g:if test="${flash.message}">
					<div class="message" role="status">
						${flash.message}
					</div>
				</g:if>
		
				<g:form controller='product' action='searchResults' align="right">
					<g:link href="#" onclick="javascript:window.history.back();"
						class="link"> << Previous page</g:link>
		   				Search: <g:textField id="searchinput" onkeypress="search()"
						name="barcode" value="${barcode}" class="search-textfield" />
					<g:submitButton name="submit" value="Search" class="submit-button" />
				</g:form>
				<table id="data-tables">
					<thead>
						<tr>
		
							<g:sortableColumn property="barcode"
								title="${message(code: 'product.barcode.label', default: 'Bar Code')}" />
		
							<g:sortableColumn property="productName"
								title="${message(code: 'product.productName.label', default: 'Product Name')}" />
		
						</tr>
					</thead>
					<tbody>
						<g:if test="${productInstanceList}">
							<div class="filteredList">
								<g:render template="/template/filter" collection="${productInstanceList }" />
							</div>
						</g:if>
		
						<g:if test="${results}">
							<div class="filteredList">
								<g:render template="/template/filter" collection="${results }" />
							</div>
						</g:if>
		
					</tbody>
				</table>
				<div>
				<g:if test="${productCount}">
					<div>
						<g:render template="/template/paginate"
							collection="${productCount }" />
					</div>
				</g:if>
				<g:if test="${productInstanceCount}">
					<div>
						<g:render template="/template/paginate"
							collection="${productInstanceCount }" />
					</div>
				</g:if>
				</div>
			</div>
			<script type="text/javascript">

			$("#searchinput").keyup(function () {
			    var value = this.value.toLowerCase().trim();

			    $("table tr").each(function (index) {
			        if (!index) return;
			        $(this).find("td").each(function () {
			            var id = $(this).text().toLowerCase().trim();
			            var not_found = (id.indexOf(value) == -1);
			            $(this).closest('tr').toggle(!not_found);
			            return not_found;
			        });
			    });
			});</script>
		</body>

</html>
