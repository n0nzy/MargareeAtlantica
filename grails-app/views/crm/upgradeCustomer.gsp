<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta name="layout" content="main" />
<title>Manage Customer Page</title>
</head>

<body>
	<div class="row-fluid">
		<ma:pageTitle title="Customer Status Management" subTitle="Upgrade Type" />
	</div>
	<div class="row-fluid">
		<div class="span7">
			<section class="widget">
				<div class="body">
					<p>
						<g:if test="${!messages.isEmpty()}">
							${messages[0]}
						</g:if>
					</p>
					<table class="table table-striped table-images">
						<thead>
							<tr>
								<th> Name</th>
								<th>Email Address</th>
								<th>Status</th>
								<th>Total Orders</th>
								<th>Amount Spent</th>
								<th></th>
								<th style="text-align: right"></th>
							</tr>
						</thead>
						<tbody>
							<g:each var="customer" in="${customers}">
								<form method="POST" action="">
									<tr>
										<td style="text-align: center">
											${customer.name}
										</td>
										<td style="text-align: center">
											${customer.email_id}
										</td>
										<td style="text-align: center"><g:if
												test="${customer.type == 'S'}">Silver</g:if> <g:elseif
												test="${customer.type == 'G'}">Gold</g:elseif> <g:elseif
												test="${customer.type == 'P'}">Platinum</g:elseif></td>
										<td style="text-align: center">
											${customer.total_orders}
										</td>
										<td style="text-align: center">
											<g:if test="${customer.total_spent == 0}">0</g:if> 
											<g:elseif	test="${customer.total_spent == null}">0</g:elseif> 
											<g:elseif	test="${customer.total_spent != 0}"> ${customer.total_spent}</g:elseif>
										</td>
										<td style="text-align: right">
										<!-- Only upgrade silver to gold if order spent is over $500 -->
											<g:if test="${customer.type == 'S'}">
												<g:if test="${customer.total_spent >= 500}">
												<button	class="btn btn-primary" type="submit" >Upgrade</button>
												</g:if>
												<g:else>
												<button type="button" class="btn btn-info disabled" 
												disabled="disabled"  data-placement="top" 
												data-original-title="Spending limit not reached">Can't Upgrade</button>
												</g:else>
											</g:if> 
												<!-- Only upgrade gold to platinum if order spent is over $1000 -->
											<g:if test="${customer.type == 'G'}">
												<g:if test="${customer.total_spent >= 1000}">
												<button	class="btn btn-primary" type="submit">Upgrade</button>
												</g:if>
												<g:else>
												<button type="button" class="btn btn-info disabled" disabled="disabled">Can't Upgrade</button>
												</g:else>
											</g:if> 
										</td>
									
									</tr>
									<input type="hidden" name="customerID"
										value="${customer.customer_id}">
								</form>
							</g:each>
						</tbody>
					</table>
				</div>
			</section>
		</div>
		<div class="span4">
            <section class="widget">
                <header>
                    <h4><i class="icon-lightbulb"></i> Information</h4>
                </header>
                <div class="body">
                    <p>This page is designed to manage Customer Status. Upgrade criteria is
		            dependent customer spending.</p>
		            <p>Silver customers can be upgraded to Gold if they have spent $500 or more.</p>
		            <p>Gold customers can be upgraded to Platinum if they have spent $1000 or more.</p>
                </div>
            </section>
        </div>
	</div>
</body>


</html>