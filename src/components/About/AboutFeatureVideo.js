import React, { useState } from 'react';
import { Modal } from 'react-responsive-modal';
import ReactPlayer from "react-player";

const AboutFeatureVideo = () => {
   const [open, setOpen] = useState(false);
   const onOpenModal = () => setOpen(true);
   const onCloseModal = () => setOpen(false)

    return (
      <React.Fragment>
      <Modal
          open={open}
          onClose={onCloseModal}
          styles={{
          modal: {
              maxWidth: "unset",
              width: "70%",
              padding: "unset"
          },
          overlay: {
              background: "rgba(0, 0, 0, 0.5)"
          },
          closeButton: {
              background: "yellow"
          }
          }}
          center
      >
          <ReactPlayer
          url="https://youtu.be/YRIHdCJqQOg"
          width="100%"
          height="calc(100vh - 200px)"
          />
      </Modal>
        <div className="features-video-area">
         <div className="container">
            <div className="row justify-content-center">
               <div className="col-lg-10 col-md-9">
                  <div className="">
                     <div className="features-video-wrapper">
                        <div className="features-shape-wrapper-1">
                           <img className="features-shape" src="assets/img/shape/features-shape.png" alt="features-shape"/>
                           Get <span>help</span> from <br/> this video

                        </div>
                        <div className="features-shape-werapper-2">
                           <span>65k+</span> <br/>views daily
                           <img className="features-shape-2" src="assets/img/shape/features-shape-2.png" alt="features-shape-2"/>
                        </div>
                        <div className="reatures-video-thumb">
                           <img src="assets/img/features/features.jpg" alt="features-img"/>
                        </div>
                        <div className="features-video-content">
                           <div className="features-btn">
                              <span className="popup-video" onClick={onOpenModal}><i className="fas fa-play"></i></span>
                           </div>
                           <div className="video-btn-text">
                              <span>Watch Video
                                 <br/>Intro</span>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      </React.Fragment>
    );
};

export default AboutFeatureVideo;