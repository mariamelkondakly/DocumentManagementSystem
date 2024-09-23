
 const parseDate = (dateStr) => {
    const parts = dateStr.split(' ');
    if (parts.length !== 2) {
      console.error(`Invalid format: ${dateStr}`);
      return new Date(NaN); // Return an invalid date
    }
  
    const [day, month, year] = parts[0].split('-');
    const [hour, minute, second] = parts[1].split(':');
  
    if (!day || !month || !year || !hour || !minute || !second) {
      console.error(`Incomplete date parts: ${dateStr}`);
      return new Date(NaN); // Return an invalid date
    }
  
    // Convert to ISO 8601 format: "YYYY-MM-DDTHH:MM:SSZ"
    const isoDateStr = `${year}-${month}-${day}T${hour}:${minute}:${second}Z`;
    const dateObj = new Date(isoDateStr);
  
    if (isNaN(dateObj.getTime())) {
      console.error(`Invalid date conversion: ${dateStr} -> ${isoDateStr}`);
    }
  
    return dateObj;
  };
  export default parseDate;