<g:if test="${it}">
	<div class="pagination" style="position:sticky;">
		<g:paginate max="10" next="next" total="${it ?: 0}" />
	</div>
</g:if>