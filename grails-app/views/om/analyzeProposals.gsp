<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Analyze Proposals</title>
<script>
function doSubmit(docForm, proposalAction) {
	docForm.elements["proposalAction"].value = proposalAction;
	docForm.submit();
}
</script>
</head>
<body>
    <div class="row-fluid">
        <div class="span7 offset1">
            <section class="widget">
                <header>
                    <h4>Analyze Product Proposals</h4>
                </header>
                <div class="body">
                    <g:each var="message" in="${messages}"><p>${message}</p></g:each>
                    <p>&nbsp;</p>
                    <g:if test="${proposals.size() == 0}">
                    <p>There are no Product Proposals to analyze at this time.</p>
                    </g:if>
                    <g:else>
                    <table class="table table-hover table-condensed">
                                    <thead>
                                        <tr>
                                            <th width="15%">Proposal ID</th>
                                            <th width="25%">Product Name</th>
                                            <th width="20%">Category</th>
                                            <th width="10%">Quantity</th>
                                            <th width="10%">Votes</th>
                                            <th width="20%" style="text-align: right"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:each var="proposal" in="${proposals}">
                                        <form class="form-horizontal" action="" method="POST" id="pForm${proposal.id}" name="pForm${proposal.id}" novalidate="novalidate" data-validate="parsley">
                                        <fieldset>
                                        <tr>
                                            <td>${proposal.id}</td>
                                            <td>${proposal.name}</td>
                                            <td>${proposal.category}</td>
                                            <td><input style="width:50px" type="number" id="quantity" name="quantity" data-trigger="change" required="required" data-range="[1, 999999]" data-validation-minlength="1" value="${proposal.quantity}" /></td>
                                            <td>${proposal.votes}</td>
                                            <td style="text-align: right">
                                                <button type="button" onclick="doSubmit(document.pForm${proposal.id}, 'A')" class="btn btn-small btn-success">Approve</button>&nbsp;&nbsp;&nbsp; <button type="button" onclick="doSubmit(document.pForm${proposal.id}, 'R')" class="btn btn-small btn-danger">Reject</button>
                                            </td>
                                        </tr>
                                        </fieldset>
                                        <input type="hidden" name="proposalID" value="${proposal.id}"/>
                                        <input type="hidden" name="proposalAction" value=""/>
                                        </form>
                                        </g:each>
                                      </tbody>
                    </table>
                    </g:else>
                </div>
            </section>
        </div>
        <div class="span4">
            <section class="widget">
                <header>
                    <h4><i class="icon-lightbulb"></i> Information</h4>
                </header>
                <div class="body">
                    <p>Approve only if there are more than 2 votes for a product!</p>
                </div>
            </section>
        </div>
    </div>
</body>
</html>