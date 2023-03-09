import React from 'react';
import dynamic from 'next/dynamic';
import Breadcrumb from '../Common/Breadcrumb';
import MembershipPrice from './MembershipPrice';
const TestimonialSlider = dynamic(() => import('../Elements/Slider/TestimonialSlider'), {
    ssr: false
  })

const MembershipMain = () => {
    return (
        <main>
            <Breadcrumb breadcrumbTitle="Membership Plan" breadcrumbSubTitle="Membership Plan" />
            <MembershipPrice />
            <TestimonialSlider />
        </main>
    );
};

export default MembershipMain;