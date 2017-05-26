
	<g:each in="${it}" status="i" var="productInstance">
		<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">						
			<td><g:link action="show" id="${productInstance.barcode}">${fieldValue(bean: productInstance, field: "barcode")}</g:link></td>
			<td>${fieldValue(bean: productInstance, field: "productName")}</td>					
		</tr>
	</g:each>


