<!DOCTYPE html>
<html>
<head>
    <title><g:layoutTitle default="e-Retail Order Management"/></title>
    <link href="${resource(dir: 'css', file: 'application.min.css')}" rel="stylesheet" />
    <link rel="shortcut icon" href="${resource(dir: 'img', file: 'favicon.png')}" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="description" content="" />
    <meta name="author" content="Margaree Atlantica" />
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- jquery and friends -->
<script src="${resource(dir: 'lib/jquery', file: 'jquery.1.9.0.min.js')}"> </script>
<script src="${resource(dir: 'lib/jquery', file: 'jquery-migrate-1.1.0.min.js')}"> </script>
<script src="${resource(dir: 'lib', file: 'jquery-ui-1.10.1.custom.js')}"> </script>
<script src="${resource(dir: 'lib', file: 'jquery.slimscroll.js')}"> </script>

<!-- jquery plugins -->
<script src="${resource(dir: 'lib/jquery-maskedinput', file: 'jquery.maskedinput.js')}"></script>
<script src="${resource(dir: 'lib/parsley', file: 'parsley.js')}"> </script>
<script src="${resource(dir: 'lib/uniform/js', file: 'jquery.uniform.js')}"></script>
<script src="${resource(dir: 'lib', file: 'select2.js')}"></script>

<!--backbone and friends -->
<script src="${resource(dir: 'lib/backbone', file: 'underscore-min.js')}"></script>
<script src="${resource(dir: 'lib/backbone', file: 'backbone-min.js')}"></script>
<script src="${resource(dir: 'lib/backbone', file: 'backbone.localStorage-min.js')}"></script>

<!-- bootstrap default plugins -->
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-transition.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-collapse.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-alert.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-tooltip.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-popover.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-button.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-dropdown.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-tab.js')}"></script>
<script src="${resource(dir: 'js/bootstrap', file: 'bootstrap-modal.js')}"></script>

<!-- bootstrap custom plugins -->
<script src="${resource(dir: 'lib', file: 'bootstrap-datepicker.js')}"></script>
<script src="${resource(dir: 'lib/bootstrap-select', file: 'bootstrap-select.js')}"></script>
<script src="${resource(dir: 'lib', file: 'jquery.bootstrap.wizard.js')}"></script>
<script src="${resource(dir: 'lib/wysihtml5', file: 'wysihtml5-0.3.0_rc2.js')}"></script>
<script src="${resource(dir: 'lib/bootstrap-wysihtml5', file: 'bootstrap-wysihtml5.js')}"></script>

<!-- basic application js-->
<script src="${resource(dir: 'js', file: 'app.js')}"></script>
<script src="${resource(dir: 'js', file: 'settings.js')}"></script>

<!-- page forms and wizards js -->
<script src="${resource(dir: 'js', file: 'forms.js')}"></script>
<script src="${resource(dir: 'js', file: 'wizard.js')}"></script>
    <g:layoutHead/>
    <r:layoutResources />
</head>
<body class="background-dark" style="zoom: 1;">
<div class="text-align-center" style="margin:20px">
    <g:link controller="index"><img src="${resource(dir: 'img', file: 'logo-site.png')}"/></g:link>
</div>
<div class="single-widget-container">
    <g:layoutBody/>
</div>

<r:layoutResources />
</body></html>