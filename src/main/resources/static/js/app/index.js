const main = {
    init : function(){
        let _this = this;
        let btnSave = document.querySelector('#btn-save');
        if(btnSave){
            btnSave.addEventListener('click',function(){
                _this.save();
            });
        }

        let btnUpdate = document.querySelector("#btn-update");
        if(btnUpdate){
            btnUpdate.addEventListener('click',function(){
                _this.update();
            });
        }

        let btnDelete = document.querySelector("#btn-delete");
        if(btnDelete){
            btnDelete.addEventListener('click',function(){
                _this.delete();
            })
        }



    },

    save : function(){
        let title = document.querySelector('#title').value;
        let author = document.querySelector('#author').value;
        let content = document.querySelector('#content').value;
        fetch("/api/v1/posts",{
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({
                title,author,content
            }),
        }).then((res)=>{
            console.log(res)
            if(!res.ok) throw new Error("Error");
            alert("글이 등록되었습니다.");
            window.location.href="/";
        }).catch((e)=>{
            alert(e);
        });
    },

    update: function(){
        let title = document.querySelector("#title").value;
        let content = document.querySelector("#content").value;

        let id = document.querySelector("#id").value;

        console.log(title);
        console.log(content);


        fetch("/api/v1/posts/"+id,{
            method:"PUT",
            headers:{
                "Content-Type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({
                title,content
            })
        }).then((res)=>{
            if(!res.ok) throw new Error("Error");
            alert("글이 수정되었습니다.");
            window.location.href="/";
        }).catch(e=>{
            alert(e);
        })
    },

    delete : function(){
        let id = document.querySelector("#id").value;
        fetch("/api/v1/posts/"+id,{
            method:"DELETE"
        }).then(res=>{
            if(!res.ok) throw new Error("Error");
            alert("글이 삭제되었습니다.")
            window.location.href="/";
        }).catch(e=>{
            alert(e);
        })

    }

}


main.init();

