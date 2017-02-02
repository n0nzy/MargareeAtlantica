<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Your Profile</title>
</head>
<body>
    <div class="row-fluid">
        <ma:pageTitle title="Your Profile" subTitle="modify your account details and password"/>
    </div>
    <div class="row-fluid">
        <div class="span7">
            <section class="widget">
                <header>
                    <h4><i class="icon-user"></i> Account Profile</h4>
                </header>
                <div class="body">
                    <g:if test="${submitted == true && errors.isEmpty() == true}">
                    <p>Account details updated successfully.</p>
                    </g:if>
                    <g:elseif test="${errors.isEmpty() == false}">
                    <div class="offset2">
                        <g:each var="error" in="${errors}">
                        <p>ERROR: <strong>${error}</strong></p>
                        </g:each>
                    </div>
                    </g:elseif>
                    <form class="form-horizontal label-left" action="${createLink(action:'index')}" method="post" novalidate="novalidate" data-validate="parsley">
                        <fieldset>
                            <legend class="section">Personal Info</legend>
                            <g:if test="${employee}">
                            <div class="control-group">
                                <label class="control-label" for="name">Your Name</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="name" name="name" data-trigger="change" maxlength="20" required="required" value="${employee.name}" />
                                </div>
                            </div>
                            </g:if>
                            <g:else>
                            <div class="control-group">
                                <label class="control-label" for="name">Name</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="name" name="name" data-trigger="change" maxlength="20" required="required" value="${customer.name}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="address">Address</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="address" name="address" data-trigger="change" maxlength="30" required="required" value="${customer.address}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="resident_phone">Home Phone</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="resident_phone" name="resident_phone" data-trigger="change" maxlength="12" required="required" value="${customer.residentPhone}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="mobile_phone">Mobile Phone</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="text" id="mobile_phone" name="mobile_phone" data-trigger="change" maxlength="12" required="required" value="${customer.mobilePhone}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="email_id">Email</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="email" id="email_id" name="email_id" data-trigger="change" data-validation-minlength="1" maxlength="25" required="required" value="${customer.email}" />
                                </div>
                            </div>
                            <div class="control-group">
                                <label for="country_id" class="control-label">Country</label>
                                <div class="controls">
                                    <select id="country_id" data-placeholder="Select country" required="required" class="span7 chzn-select" name="country_id">
                                        <g:each var="country_id" in="${countries.keySet()}">
                                        <option value="${country_id}" <g:if test="${country_id == customer.countryID}">selected</g:if> >${countries[country_id]}</option>
                                        </g:each>
                                    </select>
                                </div>
                            </div>
                            </g:else>
                            <legend class="section">Password</legend>
                            <div class="control-group">
                                <label class="control-label" for="password">New Password<br/>(leave empty for no change)</label>
                                <div class="controls controls-row">
                                    <div class="input-prepend span11">
                                        <span class="add-on pull-left"><i class="icon-lock"></i></span>
                                        <input class="span7" type="password" id="password" name="password" data-trigger="change" maxlength="15" />
                                    </div>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="password_confirm">Confirm New Password</label>
                                <div class="controls controls-row">
                                    <div class="input-prepend span11">
                                        <span class="add-on pull-left"><i class="icon-lock"></i></span>
                                        <input class="span7" type="password" id="password_confirm" name="password_confirm" data-trigger="change" data-equalto="#password" />
                                    </div>
                                </div>
                            </div>
                            <div class="form-actions">
                                <button type="submit" class="btn btn-success">Update Details</button>
                                <button type="button" class="btn">Cancel</button>
                            </div>
                        </fieldset>
                        <input type="hidden" name="submitted" id="submitted" value="true"/>
                    </form>
                </div>
            </section>
        </div>
    </div>
</body>
</html>