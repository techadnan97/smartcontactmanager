
function toggleBar() {
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none");
		$(".contant").css("margin-left","0%");
		
	}
	else{
		$(".sidebar").css("display","block");
		$(".contant").css("margin-left","20%");
	}
	
}

function deleteContact(id , page){
	swal({
		  title: "Are you sure?",
		  text: "Once deleted, you will not be able to recover this Contact!",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((willDelete) => {
		  if (willDelete) {
		   window.location="/user/"+id+"/detete-contact/"+page;
		  } else {
		    swal("Your Contact  is safe!");
		  }
		});
}


function search(){
	let query=$("#search-input").val();
	if(query==''){
		$("#seacrh-result").hide();
	}
	else{
		let url=`http://localhost:8091/serach/${query}`;
		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			let text=`<div class='list-group'>`;
			data.forEach(contact => {
				text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-action'>${contact.name}</a>`;
			});

			text+=`</div>`;
			$(".seacrh-result").html(text);
			$(".seacrh-result").show();
		});
		
	}
}