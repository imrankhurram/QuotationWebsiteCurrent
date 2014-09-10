package com.nextcontrols.bureaudao;

import java.util.List;
import java.util.Map;

import com.nextcontrols.bureaudomain.LetterTemplate;
import com.nextcontrols.bureaudomain.Template;

public interface ITemplateDAO {
public int saveTemplate(Template template);
public List<Template> getUsersSavedTemplates(int user_id);
public List<Template> getAllSavedTemplates();
public boolean deleteTemplate(Template template);
public boolean modifyTemplate(Template template);
}
