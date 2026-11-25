import pypdf
import re
import json

def parse_thapar_pdf():
    pdf_path = 'src/main/resources/syllabus/thapar_becoe_syllabus.pdf'
    reader = pypdf.PdfReader(pdf_path)
    
    courses = {}
    current_course = None
    
    # Exclude non-course headings
    excluded_headers = {'L T P CR', 'EVALUATION SCHEME', 'SR. NO.', 'COURSE NO.', 'TOTAL'}
    
    stop_headings = [
        'laboratory work', 'laboratory work:', 'course outcomes', 
        'course outcomes(cos)', 'course outcome', 'text books', 'text books:', 
        'reference books', 'reference books:', 'evaluation scheme'
    ]
    
    course_header_regex = re.compile(r'^([A-Z]{2,4}\d{3,4})\s*[:\-–]\s*([^\n\r]+)$')
    
    for page_idx, page in enumerate(reader.pages):
        text = page.extract_text() or ''
        lines = text.split('\n')
        
        for line in lines:
            line_str = line.strip()
            if not line_str:
                continue
                
            m = course_header_regex.match(line_str)
            if m:
                code = m.group(1).upper()
                name = m.group(2).strip()
                if name.upper() not in excluded_headers and len(name) > 3:
                    if code not in courses:
                        current_course = {
                            'code': code,
                            'name': name,
                            'raw_units': []
                        }
                        courses[code] = current_course
                    else:
                        current_course = courses[code]
                continue
            
            if current_course:
                current_course['raw_units'].append(line_str)
    
    print(f"Extracted {len(courses)} courses from PDF.")
    
    # Now parse units and topics for each course
    catalog = {}
    
    for code, c_data in courses.items():
        name = c_data['name']
        raw_lines = c_data['raw_units']
        
        units = []
        current_unit = None
        in_syllabus = False
        
        for line in raw_lines:
            line_lower = line.lower()
            
            # Check for stop headings
            if any(line_lower.startswith(sh) for sh in stop_headings):
                in_syllabus = False
                continue
                
            if 'syllabus' in line_lower or 'course objective' in line_lower:
                in_syllabus = True
                if 'syllabus' in line_lower:
                    continue
            
            if not in_syllabus:
                # Sometimes syllabus starts immediately after L T P Cr line without explicit 'Syllabus' label
                if ':' in line and not line.startswith('Approved') and not line.startswith('Revised'):
                    in_syllabus = True
                else:
                    continue
            
            # Unit title format: "Unit Name: topic1, topic2..." or "Unit Name - ..."
            # Usually: "Software Engineering and Processes: Introduction..."
            unit_match = re.match(r'^([A-Za-z0-9\s,\-–\(\)\/]{3,70}?):\s*(.*)$', line)
            if unit_match and not line.lower().startswith('course objective') and not line.lower().startswith('approved'):
                u_title = unit_match.group(1).strip()
                rest = unit_match.group(2).strip()
                
                # Check if u_title is not a generic text
                if len(u_title) > 3 and not u_title.lower().startswith('http') and not u_title.lower().startswith('page'):
                    current_unit = {
                        'title': u_title,
                        'topics_text': [rest] if rest else []
                    }
                    units.append(current_unit)
                    continue
            
            if current_unit and not line.startswith('Approved') and not line.startswith('Revised'):
                current_unit['topics_text'].append(line)
        
        # Parse topics for each unit
        parsed_units = []
        unit_num = 1
        for u in units:
            full_unit_text = ' '.join(u['topics_text']).strip()
            if not full_unit_text:
                continue
                
            # Topics are separated by periods, commas, or semicolons
            # Split by period or semicolon
            raw_parts = re.split(r'[;\.]\s+', full_unit_text)
            clean_topics = []
            
            for part in raw_parts:
                part = part.strip()
                if not part:
                    continue
                # If part has comma lists, e.g. "Arrays, Strings and string processing, Linked lists"
                if len(part) > 80 and ',' in part:
                    sub_parts = part.split(',')
                    for sp in sub_parts:
                        sp_clean = sp.strip().rstrip('.')
                        if len(sp_clean) > 3 and len(sp_clean) < 90:
                            clean_topics.append(sp_clean)
                else:
                    part_clean = part.strip().rstrip('.')
                    if len(part_clean) > 3 and len(part_clean) < 110:
                        clean_topics.append(part_clean)
            
            # De-duplicate while preserving order
            seen = set()
            filtered_topics = []
            for t in clean_topics:
                t_lower = t.lower()
                if t_lower not in seen and not t_lower.startswith('approved') and not t_lower.startswith('revised') and not t_lower.startswith('course outcome'):
                    seen.add(t_lower)
                    filtered_topics.append(t)
            
            if filtered_topics:
                parsed_units.append({
                    'unitNumber': unit_num,
                    'title': u['title'],
                    'topics': filtered_topics
                })
                unit_num += 1
        
        if parsed_units:
            catalog[code] = {
                'code': code,
                'name': name,
                'units': parsed_units
            }
    
    print(f"Successfully processed {len(catalog)} courses with structured units and topics.")
    
    with open('src/main/resources/syllabus/thapar_curriculum_catalog.json', 'w', encoding='utf-8') as f:
        json.dump(catalog, f, indent=2, ensure_ascii=False)
    
    print("Saved catalog to src/main/resources/syllabus/thapar_curriculum_catalog.json")

if __name__ == '__main__':
    parse_thapar_pdf()
