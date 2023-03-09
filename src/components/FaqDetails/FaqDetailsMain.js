import React from 'react';
import BreadcrumbTwo from '../Common/BreadcrumbTwo';
import FaqContentSection from './FaqContentSection';

const FaqDetailsMain = () => {
    return (
        <main>
            <BreadcrumbTwo breadcrumbTitleTwo={<>Welcome to help and support center! <br/>How may we help you?</>} breadcrumbSubTitleTwo="Help FAQ" />
            <FaqContentSection />
        </main>
    );
};

export default FaqDetailsMain;