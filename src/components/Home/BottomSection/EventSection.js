import React from "react";
import Link from "next/link";
import dynamic from "next/dynamic";
const TestimonialSliderThree = dynamic(
  () => import("../../Elements/Slider/TestimonialSliderThree"),
  {
    ssr: false,
  }
);

const EventSection = () => {
  return (
    <div className="event-area pt-110 pb-90">
      <div className="event-shape-wrapper position-relative">
        <div className="event-shape">
          <img src="assets/img/shape/feedback-img.png" alt="image not found" />
        </div>
      </div>
      <div className="container">
        <div className="row">
          <div className="col-xl-12 col-lg-12 col-12">
            <TestimonialSliderThree />
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventSection;
