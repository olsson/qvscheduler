<#list positions as pos>
.label-${pos.name?lower_case?replace(' ','')} {
	background-color: ${pos.color};
}
</#list>