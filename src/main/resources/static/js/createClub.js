function createClub() {
	console.log("클럽 생성");
	var name = $("#clname").val().trim(); // 스페이스바만 있는것 방지 
	var length = $("#clname").val().length; 
	var explain = $("#clexplain").val();
	console.log(name);
	console.log(explain);
	console.log("length " +length);
	if (name == "") {
		alert("협업공간 이름을 작성하십시오");
		return false;
	} else if (length > 25) {
		alert("협업공간 25자까지 입력 가능합니다.");
		return false; 
	}
	document.frm.submit(); 
}
function checkLength() {
	checkEnter();
	var content = $('#clexplain').val();
    $('#counter').html("("+content.length+" / 최대 150자)");    //글자수 실시간 카운팅

    if (content.length > 150){
        $(this).val(content.substring(0, 150));
        $('#counter').html("(150 / 최대 150자)");
        alert("최대 150자까지 입력 가능합니다.");
        return false;  
    }
}

function checkEnter() { 
	 var rows = $('#clexplain').val().split('\n').length;
            var maxRows = 2; 
            if( rows > maxRows){
                alert('엔터는 2번만 가능합니다');
                modifiedText = $('#clexplain').val().split("\n").slice(0, maxRows);
                $('#clexplain').val(modifiedText.join("\n"));
                return false; 
            }     
}

