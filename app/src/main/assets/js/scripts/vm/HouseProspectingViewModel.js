(function(){

    this.HouseProspectingViewModel = function() {
        this.name = "HouseProspectingViewModel";
        this.init();
        this.newphotos = [];
        this.modifiedphotos = [];
        this.pictemid = 0;
    };
    this.HouseProspectingViewModel.prototype = new unify.ViewModel();
    this.HouseProspectingViewModel.prototype.constructor = this.HouseProspectingViewModel;

    this.HouseProspectingViewModel.prototype.uploadImages = function(params) {
        print("params is " + params);
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.addPhotos(condition.delCode, condition.pics, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_UPLOAD_IMGS_RESULT, response);
            if(response.isSuccess) {
                unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getHouseDetailRequest(self, condition.delCode);
            }
        });
    };

    
    // //add photo to array when user add a photo to prospecting view
    // this.HouseProspectingViewModel.prototype.addPhoto = function(imagePath, description, imageType) {
    //     //upload image
    //     var self = this;
    //     var photo = {
    //         pic : imagePath,
    //         desc : description,
    //         type : imageType,
    //         dirty : true,
    //         photoId : self.pictemid,
    //     };
    //     self.pictemid++;
    //     this.newphotos.push(photo);
    // };
 
    // this.HouseProspectingViewModel.prototype.modified = function(photoId,description) {
    //     //alert the description of photo
    //     var self = this;
    //     for (var i = self.pictemid; i >= 0; i--) {
    //        if (this.newphotos[i].photoId = photoId) {
    //             this.newphotos[i].desc = description;
    //             this.newphotos[i].dirty = true;
    //        };
    //     };
    // };


    // //submit photos to server when user click submit button
    // this.HouseProspectingViewModel.prototype.submitPhotos = function() {
    //     //for each photos array, find dirty data
    //     var self = this;
    //     for (var i = self.pictemid; i >= 0; i--) {
    //        if (this.newphotos[i].dirty = true) {
    //             //update-imageinfo
    //        };
    //     };
    // };

    // //delete photo in server when user delete a photo
    // this.HouseProspectingViewModel.prototype.delPhoto = function(photoId) {
    //     //first, delete the phone in local array
    //     //then delete it via http 
    //     var self = this;
    //     for (var i = self.pictemid; i >= 0; i--) {
        
    //        if (this.newphotos[i].photoId = photoId) {
    //             //删除照片
    //             this.newphotos[i].photoId = -1;
                
    //        };
    //     };
    // };

    // //add serval photos
    // this.HouseProspectingViewModel.prototype.addPhotos = function(photoId) {
    //     //add photos to current photoArray

    // };

}.call(this));