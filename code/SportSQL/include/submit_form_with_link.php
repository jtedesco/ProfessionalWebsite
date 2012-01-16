<script language="JavaScript" type="text/javascript">
function submit_login_form () {
  document.loginform.submit() ;
}

function submit_user_data_form () {
  document.updateform.submit() ;
}

function submit_hidden_form (sortcolumn) {
  document.hiddenform.sortcolumn.value = sortcolumn;
  document.hiddenform.submit() ;
}

</script>