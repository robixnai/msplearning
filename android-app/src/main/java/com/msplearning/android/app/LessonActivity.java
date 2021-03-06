package com.msplearning.android.app;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import android.annotation.SuppressLint;
import android.widget.EditText;

import com.msplearning.android.app.generic.GenericAsyncAuthActivity;
import com.msplearning.android.app.rest.LessonRestClient;
import com.msplearning.entity.Lesson;

/**
 * The DisciplineManagerActivity class.
 *
 * @author Venilton Falvo Junior (veniltonjr)
 */
@EActivity(R.layout.activity_lesson)
public class LessonActivity extends GenericAsyncAuthActivity<MSPLearningApplication> {

	/**
	 * EXTRA_KEY_LESSON Intent extra key.
	 */
	public static final String EXTRA_KEY_LESSON = "E.lesson";
	/**
	 * EXTRA_KEY_ID_DISCIPLINE Intent extra key.
	 */
	public static final String EXTRA_KEY_ID_DISCIPLINE = "E.discipline.id";

	@ViewById(R.id.discipline_name)
	EditText mName;

	@RestService
	LessonRestClient mLessonRestClient;

	private Lesson currentLesson;

	@SuppressLint("NewApi")
	@AfterViews
	void afterViews() {
		super.getActionBar().setSubtitle(R.string.app_subtitle_lesson);

		this.currentLesson = (Lesson) this.getIntent().getSerializableExtra(EXTRA_KEY_LESSON);
		if(this.currentLesson == null) {
			Long idDiscipline = this.getIntent().getLongExtra(EXTRA_KEY_ID_DISCIPLINE, 0L);
			this.currentLesson = new Lesson();
			this.currentLesson.setIdDiscipline(idDiscipline);
		} else {
			this.mName.setText(this.currentLesson.getName());
		}
		this.getIntent().removeExtra(EXTRA_KEY_LESSON);
		this.getIntent().removeExtra(EXTRA_KEY_ID_DISCIPLINE);
	}

	@Click(R.id.button_save)
	void onDisciplineSave() {
		super.showLoadingProgressDialog();

		this.currentLesson.setName(this.mName.getText().toString());

		this.saveDiscipline();
	}

	@Background
	void saveDiscipline() {
		try {
			if (this.currentLesson.getId() == null) {
				this.mLessonRestClient.insert(this.currentLesson);
			} else {
				this.mLessonRestClient.update(this.currentLesson);
			}
			this.currentLesson = null;
		} catch (Exception exception) {
			super.showDialogAlert(exception.getMessage(), null);
		} finally {
			super.dismissProgressDialog();
		}
		this.setResult(RESULT_OK);
		this.finish();
	}
}
