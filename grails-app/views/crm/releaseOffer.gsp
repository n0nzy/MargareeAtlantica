<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Release Offer</title>
</head>
<body>
<div class="row-fluid">
        <ma:pageTitle title="Release an Offer" subTitle="Create New Discounts"/>
    </div>
    <div class="row-fluid">
        <div class="span7">
            <section class="widget">
                <div class="body">
                    <g:if test="${submitted == true && errors.isEmpty() == true}">
                    <p>Discount offer posted successfully.</p>
                    </g:if>
                    <g:elseif test="${errors.isEmpty() == false}">
                    <div class="offset2">
                        <g:each var="error" in="${errors}">
                        <p>ERROR: <strong>${error}</strong></p>
                        </g:each>
                    </div>
                    </g:elseif>
                    <g:if test="${!submitted || !errors.isEmpty()}">
                    <form class="form-horizontal label-left" action="" method="post" novalidate="novalidate" data-validate="parsley">
                        <fieldset>
                            <div class="control-group">
                                <label class="control-label" for="name">Customer Status</label>
                                <div class="controls controls-row">
                                    <select id="type" data-placeholder="Select Customer Status" required="required" class="span7 chzn-select" name="type">
                                        <option value="A" <g:if test="${params['type'] == 'A'}">selected</g:if> >All Customers</option>
                                        <option value="S" <g:if test="${params['type'] == 'S'}">selected</g:if> >Silver</option>
                                        <option value="G" <g:if test="${params['type'] == 'G'}">selected</g:if> >Gold</option>
                                        <option value="P" <g:if test="${params['type'] == 'P'}">selected</g:if> >Platinum</option>
                                    </select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label for="country_id" class="control-label">Country</label>
                                <div class="controls">
                                    <select id="country_id" data-placeholder="Select country" required="required" class="span7 chzn-select" name="country_id">
                                        <option value="-1" <g:if test="${params['country_id'] == '-1'}">selected</g:if> >All Countries</option>
                                        <g:each var="country_id" in="${countries.keySet()}">
                                        <option value="${country_id}" <g:if test="${country_id.toString() == params['country_id']}">selected</g:if> >${countries[country_id]}</option>
                                        </g:each>
                                    </select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="name">Validity</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="number" id="validity" name="validity" data-trigger="change" required="required" value="${params['validity']}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="name">Discount</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="number" id="discount" name="discount" data-trigger="change" required="required" value="${params['discount']}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="name">Date</label>
                                <div class="controls controls-row">
                                    <input id="launch_date" class="form-control date-picker" name="launch_date" value="${params['launch_date']}" type="text" data-trigger="change" required="required"/>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="name">Description</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="description" name="description" data-trigger="change" required="required" value="${params['description']}" />
                                </div>
                            </div>
                            <div class="form-actions">
                                <button type="submit" class="btn btn-success">Add Offer</button>
                                <button type="button" class="btn">Cancel</button>
                            </div>
                        </fieldset>
                        <input type="hidden" name="submitted" id="submitted" value="true"/>
                    </form>
                    </g:if>
                </div>
            </section>
        </div>
        <div class="span4">
            <section class="widget">
                <header>
                    <h4><i class="icon-lightbulb"></i> Information</h4>
                </header>
                <div class="body">
                    <p>This page is designed to generate new customer discount offers</p>
                </div>
            </section>
        </div>
    </div>
</body>
</html>